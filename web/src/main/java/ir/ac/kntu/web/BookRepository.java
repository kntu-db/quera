package ir.ac.kntu.web;


import ir.ac.kntu.orm.repo.annotations.Join;
import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;

import java.util.List;

@Repository
@org.springframework.stereotype.Repository
public interface BookRepository {
    @Query("select * from book join person on personId=id")
    @Join(alias = "", path = "person")
    List<Book> findAll();

}
