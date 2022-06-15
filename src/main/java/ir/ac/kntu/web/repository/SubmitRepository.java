package ir.ac.kntu.web.repository;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.utils.repo.Repository;
import ir.ac.kntu.web.model.problem.Submit;

import java.util.List;

public interface SubmitRepository extends Repository<Submit, Integer> {

    List<Submit> findAllByUserAndProblem(User user, Problem problem);
}

