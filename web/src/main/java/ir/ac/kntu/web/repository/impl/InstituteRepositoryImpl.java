package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.edu.Institute;
import ir.ac.kntu.web.repository.InstituteRepository;
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
public class InstituteRepositoryImpl implements InstituteRepository {

    private final DataSource dataSource;

    public InstituteRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Institute> findById(Integer integer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from institute where id = ?");
            stmt.setInt(1, integer);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Institute institute = map(resultSet);
                return Optional.of(institute);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Institute> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from institute");
            var rs = stmt.executeQuery();
            var institutes = new ArrayList<Institute>(rs.getFetchSize());
            while (rs.next()) {
                Institute institute = map(rs);
                institutes.add(institute);
            }
            return institutes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Institute save(Institute institute) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into institute (name, website, type, city, \"user\") values (?, ?, ?, ?, ?)", new String[]{"id"});
            setParameters(institute, stmt);
            stmt.executeUpdate();
            var rs = stmt.getGeneratedKeys();
            rs.next();
            institute.setId(rs.getInt(1));
            return institute;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Institute institute) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update institute set name = ?, website = ?, type = ?, city = ?, \"user\" = ? where id = ?");
            setParameters(institute, stmt);
            stmt.setInt(6, institute.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Institute institute) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from institute where id = ?");
            stmt.setInt(1, institute.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(Institute institute, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, institute.getName());
        stmt.setString(2, institute.getWebsite());
        stmt.setString(3, institute.getType().name().toLowerCase());
        stmt.setInt(4, institute.getCity().getId());
        if (institute.getUser() != null)
            stmt.setInt(5, institute.getUser().getId());
        else
            stmt.setNull(5, Types.INTEGER);
    }

    private Institute map(ResultSet rs) throws SQLException {
        var institute = new Institute();
        institute.setId(rs.getInt("id"));
        institute.setName(rs.getString("name"));
        institute.setWebsite(rs.getString("website"));
        institute.setType(Institute.Type.valueOf(rs.getString("type").toUpperCase()));
        return institute;
    }
}
