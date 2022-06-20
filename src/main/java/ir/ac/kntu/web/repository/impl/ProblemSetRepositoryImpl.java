package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.edu.ClassRoom;
import ir.ac.kntu.web.model.edu.Practice;
import ir.ac.kntu.web.model.mapper.Mapper;
import ir.ac.kntu.web.model.problem.Contest;
import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.model.problem.ProblemSet;
import ir.ac.kntu.web.repository.ProblemSetRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProblemSetRepositoryImpl implements ProblemSetRepository {

    private final DataSource dataSource;
    private final Mapper<ProblemSet> mapper;
    private final Mapper<Problem> problemMapper;

    public ProblemSetRepositoryImpl(DataSource dataSource, Mapper<ProblemSet> mapper, Mapper<Problem> problemMapper) {
        this.dataSource = dataSource;
        this.mapper = mapper;
        this.problemMapper = problemMapper;
    }

    @Override
    public Optional<ProblemSet> findById(Integer integer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from problemset where id = ?");
            stmt.setInt(1, integer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapper.map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProblemSet> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from problemset");
            ResultSet rs = stmt.executeQuery();
            List<ProblemSet> problemSets = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                problemSets.add(mapper.map(rs));
            }
            return problemSets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProblemSet save(ProblemSet problemSet) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into problemset (title, description, start, end, visiblescores, visible, public, classroom, sponsor, vip) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[]{"id"});
            setParameters(problemSet, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            problemSet.setId(rs.getInt(1));
            return problemSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ProblemSet problemSet) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update problemset set title = ?, description = ?, start = ?, end = ?, visiblescores = ?, visible = ?, public = ?, classroom = ?, sponsor = ?, vip = ? where id = ?");
            setParameters(problemSet, stmt);
            stmt.setInt(11, problemSet.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(ProblemSet problemSet) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from problemset where id = ?");
            stmt.setInt(1, problemSet.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(ProblemSet problemSet, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, problemSet.getTitle());
        stmt.setString(2, problemSet.getDescription());
        stmt.setDate(3, new java.sql.Date(problemSet.getStart().getTime()));
        stmt.setDate(4, new java.sql.Date(problemSet.getEnd().getTime()));
        stmt.setBoolean(5, problemSet.getVisibleScores());
        stmt.setBoolean(6, problemSet.getVisible());
        stmt.setBoolean(7, problemSet.getIsPublic());
        if (problemSet instanceof Practice) {
            Practice p = (Practice) problemSet;
            if (p.getClassRoom() != null) {
                stmt.setInt(8, p.getClassRoom().getId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }
            stmt.setNull(9, Types.VARCHAR);
            stmt.setNull(10, Types.BOOLEAN);
        }
        if (problemSet instanceof Contest) {
            Contest c = (Contest) problemSet;
            stmt.setNull(8, Types.INTEGER);
            stmt.setString(9, c.getSponsor());
            stmt.setBoolean(10, c.getVip());
        }
    }

    @Override
    public List<Object[]> findByClassRoom(ClassRoom classRoom) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select ps.*, count(p.id) as problemsCount from problemset ps left join problem p on ps.id = p.problemset where ps.classroom = ? group by ps.id");
            stmt.setInt(1, classRoom.getId());
            ResultSet rs = stmt.executeQuery();
            List<Object[]> problemSets = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                problemSets.add(new Object[]{
                        mapper.map(rs),
                        rs.getInt("problemsCount")
                });
            }
            return problemSets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ProblemSet> findByIdWithProblems(Integer id) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select ps.*, p.*, p.id as problemId from problemset ps left join problem p on ps.id = p.problemset where ps.id = ? order by p.number");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                var problemSet = mapper.map(rs);
                List<Problem> problems = new ArrayList<>(rs.getFetchSize());
                do {
                    Problem problem = problemMapper.map(rs);
                    problem.setId(rs.getInt("problemId"));
                    problem.setProblemSet(problemSet);
                    problems.add(problem);
                } while (rs.next());
                problemSet.setProblems(problems);
                return Optional.of(problemSet);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}