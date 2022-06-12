package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.edu.Practice;
import ir.ac.kntu.web.model.problem.Contest;
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

    public ProblemSetRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<ProblemSet> findById(Integer integer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from problemset where id = ?");
            stmt.setInt(1, integer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(map(rs));
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
                problemSets.add(map(rs));
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

    private ProblemSet map(ResultSet resultSet) throws SQLException {
        ProblemSet stmt;
        String type = resultSet.getString("type");
        switch (type) {
            case "contest":
                Contest c = new Contest();
                stmt = c;
                c.setSponsor(resultSet.getString("sponsor"));
                c.setVip(resultSet.getBoolean("vip"));
                break;
            case "practice":
                stmt = new Practice();
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
        stmt.setDescription(resultSet.getString("description"));
        stmt.setEnd(resultSet.getDate("end"));
        stmt.setId(resultSet.getInt("id"));
        stmt.setIsPublic(resultSet.getBoolean("isPublic"));
        stmt.setStart(resultSet.getDate("start"));
        stmt.setVisibleScores(resultSet.getBoolean("visibleScores"));
        stmt.setTitle(resultSet.getString("title"));
        stmt.setVisible(resultSet.getBoolean("visible"));
        return stmt;
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
}