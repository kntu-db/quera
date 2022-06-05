package ir.ac.kntu.web.repository;

import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;
import ir.ac.kntu.web.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@org.springframework.stereotype.Repository
public interface UserRepository {
    @Query("select * from \"user\"")
    List<User> findAll();

    @Query("select * from \"user\" where id = :id")
    User findById(Integer id);

    @Query("select * from \"user\" where mail = :mail")
    Optional<User> findByMail(String mail);

    @Query("insert into \"user\" (firstname, lastname, mail, password, phone, status, type, isPublic, joinedAt, birthDate)" +
            "values (:user.firstname, :user.lastname, :user.mail, :user.password, :user.phone, :user.status, :user.type, :user.isPublic, :user.joinedAt, :user.birthDate)" +
            "returning *")
    User save(User user);

//    void update(User user);
//
//    default User saveOrUpdate(User user) {
//        if (user.getId() == null) {
//            return save(user);
//        } else {
//            update(user);
//            return user;
//        }
//    }
}
