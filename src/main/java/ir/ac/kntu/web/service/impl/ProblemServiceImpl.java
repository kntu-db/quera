package ir.ac.kntu.web.service.impl;

import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.repository.ProblemRepository;
import ir.ac.kntu.web.service.ProblemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @Override
    public List<Problem> search(ProblemRepository.Criteria criteria) {
        return problemRepository.search(criteria);
    }

    @Override
    public Problem findById(Integer id) {
        return problemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Problem not found"));
    }
}
