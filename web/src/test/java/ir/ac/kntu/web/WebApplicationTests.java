package ir.ac.kntu.web;

import ir.ac.kntu.web.model.City;
import ir.ac.kntu.web.model.auth.Developer;
import ir.ac.kntu.web.model.auth.Manager;
import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.repository.CityRepository;
import ir.ac.kntu.web.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Date;
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
//        User user = userRepository.findByMail("arman@gmail.com").get();
//        user.setCity(cityRepository.findById(1).get());
//        userRepository.update(user);
//        Manager manager = new Manager();
//        manager.setFirstname("Arman");
//        manager.setLastname("Gholami");
//        manager.setMail("arman@gmail.com");
//        manager.setPassword("123456");
//        manager.setPhone("09121234567");
//        manager.setStatus("active");
//        manager.setCity(cityRepository.findById(1).get());
//        manager.setJoinedAt(new Date());
//        manager.setBirthDate(new Date());
//        userRepository.save(manager);
        City city = new City();
        city.setName("Alborz");
        city.setState("Tehran");
        city = cityRepository.save(city);
System.out.println(city);

//        System.out.println(city);
//        userRepository.findByIds(List.of(1, 2, 3)).forEach(System.out::println);
    }

}
