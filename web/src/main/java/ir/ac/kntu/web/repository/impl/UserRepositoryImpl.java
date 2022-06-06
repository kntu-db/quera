package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.orm.repo.annotations.Repository;
import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource;

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet rs = connection.createStatement()
                    .executeQuery("select * from \"user\"");
            List<User> users = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                users.add(map(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("select * from \"user\" where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return Optional.of(map(rs));
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByMail(String mail) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("select * from \"user\" where mail = ?");
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return Optional.of(map(rs));
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("insert into \"user\" (firstname, lastname, mail, password, phone, status, type, isPublic, joinedAt, birthDate) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning *");
            setParameters(user, ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return map(rs);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("update \"user\" set firstname = ?, lastname = ?, mail = ?, password = ?, phone = ?, status = ?, type = ?, isPublic = ?, joinedAt = ?, birthDate = ? where id = ?");
            setParameters(user, ps);
            ps.setInt(11, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("delete from \"user\" where id = ?");
            ps.setInt(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(User user, PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getFirstname());
        ps.setString(2, user.getLastname());
        ps.setString(3, user.getMail());
        ps.setString(4, user.getPassword());
        ps.setString(5, user.getPhone());
        ps.setString(6, user.getStatus());
        ps.setString(7, user.getType());
        ps.setBoolean(8, user.getIsPublic());
        ps.setTimestamp(9, new Timestamp(user.getJoinedAt().getTime()));
        ps.setDate(10, new Date(user.getBirthDate().getTime()));
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setFirstname(rs.getString("firstname"));
        u.setLastname(rs.getString("lastname"));
        u.setMail(rs.getString("mail"));
        u.setPassword(rs.getString("password"));
        u.setPhone(rs.getString("phone"));
        u.setStatus(rs.getString("status"));
        u.setType(rs.getString("type"));
        u.setIsPublic(rs.getBoolean("isPublic"));
        u.setJoinedAt(rs.getDate("joinedAt"));
        u.setBirthDate(rs.getDate("birthDate"));
        return u;
    }
}
