package ir.ac.kntu.web.repository;

import ir.ac.kntu.web.utils.repo.Repository;
import ir.ac.kntu.web.model.auth.User;

import java.util.Optional;


public interface UserRepository extends Repository<User, Integer> {
    Optional<User> findByMailWithAuthorities(String mail);
}
