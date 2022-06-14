package ir.ac.kntu.web.service.impl;

import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.repository.ProblemRepository;
import ir.ac.kntu.web.service.ProblemService;
import ir.ac.kntu.web.service.dto.ProblemDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Integer create(ProblemDto problemDto) {
        Problem problem = new Problem();
        problem.setTitle(problemDto.getTitle());
        problem.setScore(problemDto.getScore());
        problem.setCategory(problemDto.getCategory());
        problem.setText(problemDto.getText());
        List<String> tags = Arrays.stream(problemDto.getTags().split(",")).map(String::trim).collect(Collectors.toList());
        problem.setTags(tags);
        return problemRepository.save(problem).getId();
    }
}
