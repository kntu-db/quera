package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.magnet.Technology;
import ir.ac.kntu.web.repository.TechnologyRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TechnologyRepositoryImpl implements TechnologyRepository {

    private final DataSource dataSource;

    public TechnologyRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Technology> findById(Integer id) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from technology where id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Technology> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from technology");
            ResultSet rs = stmt.executeQuery();
            List<Technology> result = new ArrayList<>();
            while (rs.next())
                result.add(map(rs));
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Technology save(Technology technology) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into technology(title, category) values (?, ?)", new String[]{"id"});
            setParameters(technology, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            technology.setId(rs.getInt(1));
            return technology;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Technology technology) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update technology set title = ?, category = ? where id = ?");
            setParameters(technology, stmt);
            stmt.setInt(3, technology.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Technology technology) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from technology where id = ?");
            stmt.setInt(1, technology.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setParameters(Technology t, PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, t.getId());
        stmt.setString(2, t.getTitle());
        stmt.setString(3, t.getCategory());
    }

    private Technology map(ResultSet rs) throws SQLException {
        Technology t = new Technology();
        t.setId(rs.getInt("id"));
        t.setTitle(rs.getString("title"));
        t.setCategory(rs.getString("category"));
        return t;
    }
}
