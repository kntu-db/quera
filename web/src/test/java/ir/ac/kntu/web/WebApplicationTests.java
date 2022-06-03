package ir.ac.kntu.web;

import ir.ac.kntu.orm.mapping.ResultSetMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

//@SpringBootTest
class WebApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    PersonRepository personRepository;

//    @Test
    void contextLoads() throws SQLException {
//        Flyway.configure().dataSource(dataSource).load().migrate();
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT p.* FROM person p");
        ResultSetMapper<Person> mapper = new ResultSetMapper<>(Person.class, "p", resultSet);
        List<Person> result = mapper.getMappedResultList();
        System.out.println(result);
//        dataSource.getConnection().createStatement().executeQuery($S)

    }

//    @Test
    void test() {
        Person person = personRepository.findOneById(1);
        personRepository.findMaxId();
        System.out.println(person);
    }

}
