package ir.ac.kntu.web.service;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.model.problem.Submit;
import ir.ac.kntu.web.service.builder.ProblemCriteria;
import ir.ac.kntu.web.service.dto.ProblemDto;
import ir.ac.kntu.web.service.dto.ProblemViewDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProblemService {
    List<ProblemViewDto> search(ProblemCriteria criteria, User user);

    List<String> findAllTags();

    Problem findById(Integer id);

    Integer create(ProblemDto problemDto);

    void submit(Integer id, MultipartFile file, User user);

    List<Problem> solved(User user);

    List<Submit> findSubmitsByUserAndProblemId(User user, Integer id);
}
