package ir.ac.kntu.web.repository;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.utils.repo.Repository;
import ir.ac.kntu.web.model.problem.Problem;

import java.util.List;

public interface ProblemRepository extends Repository<Problem, Integer> {

    List<Object[]> search(Criteria criteria);

    List<Problem> solved(User user);

    interface Criteria {

    }
}
