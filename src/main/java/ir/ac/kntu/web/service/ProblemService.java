package ir.ac.kntu.web.service;

import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.repository.ProblemRepository;

import java.util.List;

public interface ProblemService {
    List<Problem> search(ProblemRepository.Criteria criteria);

    Problem findById(Integer id);
}
