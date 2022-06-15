package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.repository.ProblemRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
public class ProblemRepositoryImpl implements ProblemRepository {

    private final DataSource dataSource;

    public ProblemRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Problem> findById(Integer id) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from problem where id = ?");
            stmt.setInt(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Problem problem = map(resultSet);
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
                Problem problem = map(rs);
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
            var stmt = con.prepareStatement("insert into problem (category, number, score, text, title, problemset) values (?, ?, ?, ?, ?, ?)", new String[]{"id"});
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
            var stmt = con.prepareStatement("update problem set category = ?, number = ?, score = ?, text = ?, title = ?, problemset = ? where id = ?");
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
        statement.setObject(2, problem.getNumber());
        statement.setInt(3, problem.getScore());
        statement.setString(4, problem.getText());
        statement.setString(5, problem.getTitle());
        if (problem.getProblemSet() != null)
            statement.setInt(6, problem.getProblemSet().getId());
        else
            statement.setNull(6, Types.INTEGER);
    }

    private Problem map(ResultSet resultSet) throws SQLException {
        var p = new Problem();
        p.setId(resultSet.getInt("id"));
        p.setCategory(resultSet.getString("category"));
        p.setNumber(resultSet.getInt("number"));
        p.setScore(resultSet.getInt("score"));
        p.setText(resultSet.getString("text"));
        p.setTitle(resultSet.getString("title"));
        return p;
    }

    @Override
    public List<Object[]> search(Criteria criteria) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select p.*, tag, count(distinct s.\"user\") as solveCount from problem p left join problem_tag pt on p.id = pt.problem left join submit s on p.id = s.problem group by p.id, pt.tag order by p.id desc ");
            var rs = stmt.executeQuery();
            List<Object[]> res = new ArrayList<>(rs.getFetchSize());
            Problem lastProblem = null;
            while(rs.next()) {
                if (lastProblem == null || !lastProblem.getId().equals(rs.getInt("id"))) {
                    lastProblem = map(rs);
                    lastProblem.setTags(new ArrayList<>());
                    lastProblem.getTags().add(rs.getString("tag"));
                    res.add(new Object[]{lastProblem, rs.getInt("solveCount")});
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
    public List<Problem> solved(User user) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from problem p where exists(select 1 from submit s where s.problem = p.id and s.\"user\" = ? and s.score = p.score)");
            stmt.setInt(1, user.getId());
            var rs = stmt.executeQuery();
            var problems = new ArrayList<Problem>(rs.getFetchSize());
            while (rs.next()) {
                Problem problem = map(rs);
                problems.add(problem);
            }
            return problems;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public class SQLCriteria implements Criteria {

    }
}
