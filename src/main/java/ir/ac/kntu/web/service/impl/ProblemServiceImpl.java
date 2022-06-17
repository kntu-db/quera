package ir.ac.kntu.web.service.impl;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.model.problem.Submit;
import ir.ac.kntu.web.repository.ProblemRepository;
import ir.ac.kntu.web.repository.SubmitRepository;
import ir.ac.kntu.web.service.ProblemService;
import ir.ac.kntu.web.service.builder.ProblemCriteria;
import ir.ac.kntu.web.service.dto.ProblemDto;
import ir.ac.kntu.web.service.dto.ProblemViewDto;
import ir.ac.kntu.web.utils.tx.Tx;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final SubmitRepository submitRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository, SubmitRepository submitRepository) {
        this.problemRepository = problemRepository;
        this.submitRepository = submitRepository;
    }

    @Override
    public List<ProblemViewDto> search(ProblemCriteria criteria, User user) {
        return problemRepository.search(criteria, user).stream()
                .map(o -> new ProblemViewDto((Problem) o[0], (Integer) o[1], (Integer) o[2], (Boolean) o[3]))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllTags() {
        return problemRepository.findAllTags();
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

    @Tx
    @Override
    public void submit(Integer id, MultipartFile file, User user) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Problem not found"));
        Submit submit = new Submit();
        submit.setProblem(problem);
        submit.setUser(user);
        submit.setScore(problem.getScore());
        submit.setInContest(false);
        submit.setIsFinal(true);
        submit.setStatus(Submit.Status.JUDGED);
        submit.setTime(new Date());
        submit.setUri(String.format("submit/%s", file.getName()));
        submit.setType(Submit.Type.UPLOAD);
        submitRepository.save(submit);
    }

    @Override
    public List<Problem> solved(User user) {
        return this.problemRepository.solved(user);
    }

    @Override
    public List<Submit> findSubmitsByUserAndProblemId(User user, Integer id) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Problem not found"));
        return submitRepository.findAllByUserAndProblem(user, problem);
    }
}
