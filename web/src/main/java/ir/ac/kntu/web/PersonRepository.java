package ir.ac.kntu.web;

import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;

import java.util.List;

@Repository
@org.springframework.stereotype.Repository
public interface PersonRepository {

    @Query("select * from person where id=:id")
    Person findOneById(Integer id);

    @Query("select * from person")
    List<Person> findAll();

}
