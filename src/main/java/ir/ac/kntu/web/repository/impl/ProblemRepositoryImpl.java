package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.mapper.Mapper;
import ir.ac.kntu.web.model.mapper.MapperFactory;
import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.repository.ProblemRepository;
import ir.ac.kntu.web.service.builder.ProblemCriteria;
import ir.ac.kntu.web.utils.StringUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProblemRepositoryImpl implements ProblemRepository {

    private final DataSource dataSource;
    private final Mapper<Problem> problemMapper;

    public ProblemRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.problemMapper = MapperFactory.forEntity(Problem.class);
    }

    @Override
    public Optional<Problem> findById(Integer id) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from problem where id = ?");
            stmt.setInt(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Problem problem = problemMapper.map(resultSet);
                return Optional.of(problem);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Problem> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from problem");
            var rs = stmt.executeQuery();
            var problems = new ArrayList<Problem>(rs.getFetchSize());
            while (rs.next()) {
                Problem problem = problemMapper.map(rs);
                problems.add(problem);
            }
            return problems;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Problem save(Problem problem) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into problem (category, score, text, title, problemset) values (?, ?, ?, ?, ?, ?)", new String[]{"id"});
            setParameters(problem, stmt);
            stmt.executeUpdate();
            var rs = stmt.getGeneratedKeys();
            rs.next();
            problem.setId(rs.getInt(1));
            if (problem.getTags() != null) {
                stmt = con.prepareStatement("insert into problem_tag (problem, tag) values (?, ?)");
                stmt.setInt(1, problem.getId());
                for (var tag : problem.getTags()) {
                    stmt.setString(2, tag);
                    stmt.executeUpdate();
                }
            }
            return problem;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Problem problem) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update problem set category = ?, score = ?, text = ?, title = ?, problemset = ? where id = ?");
            setParameters(problem, stmt);
            stmt.setInt(7, problem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Problem problem) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from problem where id = ?");
            stmt.setInt(1, problem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(Problem problem, PreparedStatement statement) throws SQLException {
        statement.setString(1, problem.getCategory());
        statement.setInt(2, problem.getScore());
        statement.setString(3, problem.getText());
        statement.setString(4, problem.getTitle());
        if (problem.getProblemSet() != null)
            statement.setInt(5, problem.getProblemSet().getId());
        else
            statement.setNull(5, Types.INTEGER);
    }

    @Override
    public List<Object[]> search(ProblemCriteria criteria, User user) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement(
                    "select p.*, " +
                            "       tag, " +
                            "       sum(case when s.isfinal and s.score = p.score then 1 else 0 end)             as solveCount, " +
                            "       sum(case when s.isfinal then 1 else 0 end)                                   as totalTries, " +
                            "       sum(case when s.\"user\" = ? and s.score = p.score then 1 else 0 end)::integer::boolean as userSolved " +
                            "from problem p " +
                            "         left join problem_tag pt on p.id = pt.problem " +
                            "         left join submit s on p.id = s.problem " +
                            "where (? is null or p.title like ?) " +
                            (!CollectionUtils.isEmpty(criteria.getTags()) ?
                                    String.format("  and exists(select 1 from problem_tag pt2 where pt2.problem = p.id and pt2.tag in (%s)) ", criteria.getTags().stream().map(t->"?").collect(Collectors.joining(","))):
                                    " ") +
                            "group by p.id, pt.tag " +
                            "order by p.id desc");
            stmt.setInt(1, user.getId());
            String formattedQuery = StringUtil.isEmpty(criteria.getQuery()) ? null : "%" + criteria.getQuery() + "%";
            stmt.setString(2, formattedQuery);
            stmt.setString(3, formattedQuery);
            if (!CollectionUtils.isEmpty(criteria.getTags())) {
                int i = 4;
                for (String tag: criteria.getTags())
                    stmt.setString(i++, tag);
            }
            var rs = stmt.executeQuery();
            List<Object[]> res = new ArrayList<>(rs.getFetchSize());
            Problem lastProblem = null;
            while (rs.next()) {
                if (lastProblem == null || !lastProblem.getId().equals(rs.getInt("id"))) {
                    lastProblem = problemMapper.map(rs);
                    lastProblem.setTags(new ArrayList<>());
                    lastProblem.getTags().add(rs.getString("tag"));
                    res.add(new Object[]{lastProblem, rs.getInt("solveCount"), rs.getInt("totalTries"), rs.getBoolean("userSolved")});
                } else {
                    lastProblem.getTags().add(rs.getString("tag"));
                }
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> findAllTags() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select distinct tag from problem_tag");
            var rs = stmt.executeQuery();
            var tags = new ArrayList<String>(rs.getFetchSize());
            while (rs.next()) {
                tags.add(rs.getString("tag"));
            }
            return tags;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Problem> solved(User user) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from problem p where exists(select 1 from submit s where s.problem = p.id and s.\"user\" = ? and s.score = p.score)");
            stmt.setInt(1, user.getId());
            var rs = stmt.executeQuery();
            var problems = new ArrayList<Problem>(rs.getFetchSize());
            while (rs.next()) {
                Problem problem = problemMapper.map(rs);
                problems.add(problem);
            }
            return problems;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
