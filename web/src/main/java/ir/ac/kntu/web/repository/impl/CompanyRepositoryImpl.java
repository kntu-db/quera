package ir.ac.kntu.web.repository.impl;

import ir.ac.kntu.web.model.magnet.Company;
import ir.ac.kntu.web.repository.CompanyRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final DataSource dataSource;

    public CompanyRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Company> findById(Integer integer) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from company where id = ?");
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
    public List<Company> findAll() {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("select * from company");
            ResultSet rs = stmt.executeQuery();
            List<Company> companies = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                companies.add(map(rs));
            }
            return companies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Company save(Company company) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("insert into company (name, foundeddate, website, description, logo, employer, address, city, size, field) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[]{"id"});
            setParameters(company, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                company.setId(rs.getInt(1));
            }
            return company;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Company company) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("update company set name = ?, foundeddate = ?, website = ?, description = ?, logo = ?, employer = ?, address = ?, city = ?, size = ?, field = ? where id = ?");
            setParameters(company, stmt);
            stmt.setInt(11, company.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Company company) {
        try (var con = dataSource.getConnection()) {
            var stmt = con.prepareStatement("delete from company where id = ?");
            stmt.setInt(1, company.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(Company company, PreparedStatement stmt) throws SQLException {
            stmt.setString(1, company.getName());
            stmt.setDate(2, new Date(company.getFoundedDate().getTime()));
            stmt.setString(3, company.getWebsite());
            stmt.setString(4, company.getDescription());
            stmt.setString(5, company.getLogo());
            stmt.setInt(6, company.getEmployer().getId());
            stmt.setString(7, company.getAddress());
            stmt.setInt(8, company.getCity().getId());
            stmt.setInt(9, company.getSize());
            stmt.setString(10, company.getField());
    }

    private Company map(ResultSet rs) throws SQLException {
        Company c = new Company();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setFoundedDate(rs.getTimestamp("founded_date"));
        c.setWebsite(rs.getString("website"));
        c.setDescription(rs.getString("description"));
        c.setLogo(rs.getString("logo"));
        c.setAddress(rs.getString("address"));
        c.setSize(rs.getInt("size"));
        c.setField(rs.getString("field"));
        return c;
    }
}
