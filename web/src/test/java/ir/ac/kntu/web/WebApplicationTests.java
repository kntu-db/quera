package ir.ac.kntu.web;

import ir.ac.kntu.web.repository.CityRepository;
import ir.ac.kntu.web.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
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
//        User byId = userRepository.findById(1);
//        System.out.println(byId);
//        userRepository.findByMail("arman@gmail.com").ifPresent(System.out::println);
//        City city = new City();
//        city.setName("Tehran");
//        city.setState("Tehran");
//        city = cityRepository.save(city);
//        System.out.println(city);
        userRepository.findByIds(List.of(1, 2, 3)).forEach(System.out::println);
    }

}
