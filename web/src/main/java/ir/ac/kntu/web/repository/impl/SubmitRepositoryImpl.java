package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.problem.Submit;
import ir.ac.kntu.web.model.problem.SubmitStatus;
import ir.ac.kntu.web.model.problem.SubmitType;
import ir.ac.kntu.web.repository.SubmitRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubmitRepositoryImpl implements SubmitRepository {

    private final DataSource dataSource;

    public SubmitRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Submit> findById(Integer integer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from submit where id = ?");
            stmt.setInt(1, integer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Submit> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from submit");
            ResultSet rs = stmt.executeQuery();
            List<Submit> submits = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                submits.add(map(rs));
            }
            return submits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Submit save(Submit submit) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into submit (problem, \"user\", time, status, uri, type, score, incontest, isfinal) values (?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[]{"id"});
            setParameters(submit, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            submit.setId(rs.getInt(1));
            return submit;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Submit submit) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update submit set problem = ?, \"user\" = ?, time = ?, status = ?, uri = ?, type = ?, score = ?, incontest = ?, isfinal = ? where id = ?");
            setParameters(submit, stmt);
            stmt.setInt(10, submit.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Submit submit) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from submit where id = ?");
            stmt.setInt(1, submit.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(Submit submit, PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, submit.getProblem().getId());
        stmt.setInt(2, submit.getUser().getId());
        stmt.setTimestamp(3, new java.sql.Timestamp(submit.getTime().getTime()));
        stmt.setString(4, submit.getStatus().name().toLowerCase());
        stmt.setString(5, submit.getUri());
        stmt.setString(6, submit.getType().name().toLowerCase());
        stmt.setString(7, submit.getScore());
        stmt.setBoolean(8, submit.getInContest());
        stmt.setBoolean(9, submit.getIsFinal());
    }

    private Submit map(ResultSet rs) throws SQLException {
        Submit s = new Submit();
        s.setId(rs.getInt("id"));
        s.setStatus(SubmitStatus.valueOf(rs.getString("status").toUpperCase()));
        s.setTime(rs.getTimestamp("time"));
        s.setUri(rs.getString("uri"));
        s.setType(SubmitType.valueOf(rs.getString("type").toUpperCase()));
        s.setScore(rs.getString("score"));
        s.setInContest(rs.getBoolean("incontest"));
        s.setIsFinal(rs.getBoolean("isfinal"));
        return s;
    }
}
