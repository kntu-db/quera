package ir.ac.kntu.web;

import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;

@Repository
public interface PersonRepository {

    @Query("select * from person where id=:id")
    public Person findOneById(Integer id);

}
