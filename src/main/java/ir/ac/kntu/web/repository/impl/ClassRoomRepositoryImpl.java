package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.edu.ClassRoom;
import ir.ac.kntu.web.repository.ClassRoomRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClassRoomRepositoryImpl implements ClassRoomRepository {

    private final DataSource dataSource;

    public ClassRoomRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<ClassRoom> findById(Integer integer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from classroom where id = ?");
            stmt.setInt(1, integer);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                ClassRoom classRoom = map(resultSet);
                return Optional.of(classRoom);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClassRoom> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from classroom");
            var rs = stmt.executeQuery();
            var classRooms = new ArrayList<ClassRoom>(rs.getFetchSize());
            while (rs.next()) {
                ClassRoom classRoom = map(rs);
                classRooms.add(classRoom);
            }
            return classRooms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClassRoom save(ClassRoom classRoom) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into classroom (title, professor, description, phone, password, maxcount, archived, opentoregister, institute, creator, year, turn, publishafterarchive) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[]{"id"});
            setParameters(classRoom, stmt);
            stmt.executeUpdate();
            var rs = stmt.getGeneratedKeys();
            rs.next();
            classRoom.setId(rs.getInt(1));
            return classRoom;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ClassRoom classRoom) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update classroom set title = ?, professor = ?, description = ?, phone = ?, password = ?, maxcount = ?, archived = ?, opentoregister = ?, institute = ?, creator = ?, year = ?, turn = ?, publishafterarchive = ? where id = ?");
            setParameters(classRoom, stmt);
            stmt.setInt(14, classRoom.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(ClassRoom classRoom) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from classroom where id = ?");
            stmt.setInt(1, classRoom.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(ClassRoom cr, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, cr.getTitle());
        stmt.setString(2, cr.getProfessor());
        stmt.setString(3, cr.getDescription());
        stmt.setString(4, cr.getPhone());
        stmt.setString(5, cr.getPassword());
        stmt.setInt(6, cr.getMaxCount());
        stmt.setBoolean(7, cr.getArchived());
        stmt.setBoolean(8, cr.getOpenToRegister());
        stmt.setInt(9, cr.getInstitute().getId());
        stmt.setInt(10, cr.getCreator().getId());
        stmt.setInt(11, cr.getYear());
        stmt.setString(12, cr.getTurn().name().toLowerCase());
        stmt.setBoolean(13, cr.getPublishAfterArchive());
    }

    private ClassRoom map(ResultSet resultSet) throws SQLException {
        ClassRoom cr = new ClassRoom();
        cr.setId(resultSet.getInt("id"));
        cr.setDescription(resultSet.getString("description"));
        cr.setArchived(resultSet.getBoolean("archived"));
        cr.setPassword(resultSet.getString("password"));
        cr.setPhone(resultSet.getString("phone"));
        cr.setMaxCount(resultSet.getInt("maxcount"));
        cr.setOpenToRegister(resultSet.getBoolean("opentoregister"));
        cr.setProfessor(resultSet.getString("professor"));
        cr.setPublishAfterArchive(resultSet.getBoolean("publishafterarchive"));
        cr.setTurn(ClassRoom.Turn.valueOf(resultSet.getString("turn").toUpperCase()));
        cr.setYear(resultSet.getInt("year"));
        cr.setTitle(resultSet.getString("title"));
        return cr;
    }

    @Override
    public List<ClassRoom> findByUser(User user) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from classroom cr where exists(select 1 from classparticipation cp where cp.classroom = cr.id and cp.developer = ?)");
            stmt.setInt(1, user.getId());
            var rs = stmt.executeQuery();
            var classRooms = new ArrayList<ClassRoom>(rs.getFetchSize());
            while (rs.next()) {
                ClassRoom classRoom = map(rs);
                classRooms.add(classRoom);
            }
            return classRooms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isUserInClass(ClassRoom classRoom, User user) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select 1 from classparticipation cp where cp.classroom = ? and cp.developer = ?");
            stmt.setInt(1, classRoom.getId());
            stmt.setInt(2, user.getId());
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
