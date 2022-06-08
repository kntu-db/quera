package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.magnet.JobOffer;
import ir.ac.kntu.web.repository.JobOfferRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JobOfferRepositoryImpl implements JobOfferRepository {

    private final DataSource dataSource;

    public JobOfferRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<JobOffer> findById(Integer integer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from joboffer where id = ?");
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
    public List<JobOffer> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from joboffer");
            ResultSet rs = stmt.executeQuery();
            List<JobOffer> jobOffers = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                jobOffers.add(map(rs));
            }
            return jobOffers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JobOffer save(JobOffer jobOffer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into joboffer (createdat, level, cooperation, workdistance, salary, title, company, city, description) values (?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[]{"id"});
            setParameters(jobOffer, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                jobOffer.setId(rs.getInt(1));
            }
            return jobOffer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(JobOffer jobOffer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update joboffer set createdat = ?, level = ?, cooperation = ?, workdistance = ?, salary = ?, title = ?, company = ?, city = ?, description = ? where id = ?");
            setParameters(jobOffer, stmt);
            stmt.setInt(10, jobOffer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(JobOffer jobOffer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from jobOffer where id = ?");
            stmt.setInt(1, jobOffer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(JobOffer jobOffer, PreparedStatement stmt) throws SQLException {
        stmt.setTimestamp(1, new Timestamp(jobOffer.getCreatedAt().getTime()));
        stmt.setString(2, jobOffer.getLevel().name().toLowerCase());
        stmt.setString(3, jobOffer.getCooperation().name().toLowerCase());
        stmt.setBoolean(4, jobOffer.getWorkDistance());
        stmt.setInt(5, jobOffer.getSalary());
        stmt.setString(6, jobOffer.getTitle());
        stmt.setInt(7, jobOffer.getCompany().getId());
        stmt.setInt(8, jobOffer.getCity().getId());
        stmt.setString(9, jobOffer.getDescription());
    }

    private JobOffer map(ResultSet rs) throws SQLException {
        JobOffer jo = new JobOffer();
        jo.setId(rs.getInt("id"));
        jo.setTitle(rs.getString("title"));
        jo.setDescription(rs.getString("description"));
        jo.setCooperation(JobOffer.Cooperation.valueOf(rs.getString("cooperation")));
        jo.setSalary(rs.getInt("salary"));
        jo.setExpired(rs.getBoolean("expired"));
        jo.setWorkDistance(rs.getBoolean("workdistance"));
        jo.setCreatedAt(rs.getTimestamp("createdat"));
        jo.setLevel(JobOffer.Level.valueOf(rs.getString("level")));
        return jo;
    }
}
