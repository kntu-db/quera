package ir.ac.kntu.web.repository;

import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;
import ir.ac.kntu.web.model.City;

import java.util.List;

@Repository
@org.springframework.stereotype.Repository
public interface CityRepository {

    @Query("select * from city")
    public List<City> findAll();

    @Query("select * from city where id = :id")
    public City findById(Integer id);

}
