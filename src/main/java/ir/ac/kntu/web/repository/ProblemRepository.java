package ir.ac.kntu.web.repository;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.service.builder.ProblemCriteria;
import ir.ac.kntu.web.utils.repo.Repository;
import ir.ac.kntu.web.model.problem.Problem;

import java.util.List;

public interface ProblemRepository extends Repository<Problem, Integer> {

    List<Object[]> search(ProblemCriteria criteria, User user);

    List<String> findAllTags();

    List<Problem> solved(User user);

}
