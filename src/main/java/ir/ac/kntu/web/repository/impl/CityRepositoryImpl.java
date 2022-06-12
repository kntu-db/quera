package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.City;
import ir.ac.kntu.web.repository.CityRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CityRepositoryImpl implements CityRepository {

    private final DataSource dataSource;

    public CityRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<City> findById(Integer integer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from city where id = ?");
            stmt.setInt(1, integer);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                City city = map(resultSet);
                return Optional.of(city);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<City> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from city");
            var rs = stmt.executeQuery();
            var cities = new ArrayList<City>(rs.getFetchSize());
            while (rs.next()) {
                City city = map(rs);
                cities.add(city);
            }
            return cities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public City save(City city) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into city (name, state) values (?, ?)", new String[]{"id"});
            setParameters(city, stmt);
            stmt.executeUpdate();
            var rs = stmt.getGeneratedKeys();
            rs.next();
            city.setId(rs.getInt(1));
            return city;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(City city) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update city set name = ?, state = ? where id = ?");
            setParameters(city, stmt);
            stmt.setInt(3, city.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(City city) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from city where id = ?");
            stmt.setInt(1, city.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private City map(ResultSet resultSet) throws SQLException {
        City c = new City();
        c.setId(resultSet.getInt("id"));
        c.setName(resultSet.getString("name"));
        c.setState(resultSet.getString("state"));
        return c;
    }

    private void setParameters(City city, PreparedStatement statement) throws SQLException {
        statement.setString(1, city.getName());
        statement.setString(2, city.getState());
    }

}
