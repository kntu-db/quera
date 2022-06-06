package ir.ac.kntu.web.repository;

import ir.ac.kntu.utilities.repo.Repository;
import ir.ac.kntu.web.model.auth.User;

import java.util.Optional;


public interface UserRepository extends Repository<User, Integer> {
    Optional<User> findByMail(String mail);
}
