package ir.ac.kntu.web;

import ir.ac.kntu.orm.mapping.ResultSetMapper;
import ir.ac.kntu.web.model.City;
import ir.ac.kntu.web.model.User;
import ir.ac.kntu.web.repository.CityRepository;
import ir.ac.kntu.web.repository.UserRepository;
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

@SpringBootTest
class WebApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void test() {
        User byId = userRepository.findById(1);
        System.out.println(byId);
    }

}
