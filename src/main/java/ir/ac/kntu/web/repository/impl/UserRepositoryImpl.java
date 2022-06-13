package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.auth.*;
import ir.ac.kntu.web.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
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
        try (var con = dataSource.getConnection()) {
            ResultSet rs = con.createStatement()
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
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from \"user\" where id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return Optional.of(map(rs));
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByMailWithAuthorities(String mail) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from \"user\" join user_role on \"user\".id = user_role.\"user\" where mail = ?");
            stmt.setString(1, mail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = map(rs);
                if (user instanceof Manager) {
                    Manager m = (Manager) user;
                    do {
                        List<Role> roles = new ArrayList<>();
                        roles.add(mapRole(rs));
                        m.setAuthorities(roles);
                    } while (rs.next());
                }
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean emailExists(String email) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from \"user\" where mail = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Role mapRole(ResultSet rs) throws SQLException {
        return Role.valueOf(rs.getString("role").toUpperCase());
    }

    @Override
    public User save(User user) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into \"user\" (firstname, lastname, mail, password, phone, status, type, isPublic, joinedAt, birthDate, city) values (?, ?, ?, ?, ?, ?::userstatus, ?::usertype, ?, ?, ?, ?)", new String[]{"id"});
            setParameters(user, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            user.setId(rs.getInt(1));
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update \"user\" set firstname = ?, lastname = ?, mail = ?, password = ?, phone = ?, status = ?::userstatus, type = ?::usertype, isPublic = ?, joinedAt = ?, birthDate = ?, city = ? where id = ?");
            setParameters(user, stmt);
            stmt.setInt(12, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User user) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from \"user\" where id = ?");
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(User user, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, user.getFirstname());
        stmt.setString(2, user.getLastname());
        stmt.setString(3, user.getMail());
        stmt.setString(4, user.getPassword());
        stmt.setString(5, user.getPhone());
        stmt.setString(6, user.getStatus().name().toLowerCase());
        stmt.setString(7, user.getClass().getSimpleName().toLowerCase());
        if (user instanceof Developer)
            stmt.setBoolean(8, ((Developer) user).getIsPublic());
        else
            stmt.setNull(8, Types.BOOLEAN);
        stmt.setTimestamp(9, new Timestamp(user.getJoinedAt().getTime()));
        if (user.getBirthDate() != null)
            stmt.setDate(10, new Date(user.getBirthDate().getTime()));
        else
            stmt.setNull(10, Types.DATE);
        if (user.getCity() != null)
            stmt.setInt(11, user.getCity().getId());
        else
            stmt.setNull(11, Types.INTEGER);
    }

    private User map(ResultSet rs) throws SQLException {
        User u;
        String type = rs.getString("type");
        switch (type) {
            case "developer":
                Developer d = new Developer();
                d.setIsPublic(rs.getBoolean("isPublic"));
                u = d;
                break;
            case "employer":
                u = new Employer();
                break;
            case "manager":
                u = new Manager();
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
        u.setId(rs.getInt("id"));
        u.setFirstname(rs.getString("firstname"));
        u.setLastname(rs.getString("lastname"));
        u.setMail(rs.getString("mail"));
        u.setPassword(rs.getString("password"));
        u.setPhone(rs.getString("phone"));
        u.setStatus(User.Status.valueOf(rs.getString("status").toUpperCase()));
        u.setJoinedAt(rs.getDate("joinedAt"));
        u.setBirthDate(rs.getDate("birthDate"));
        return u;
    }
}
