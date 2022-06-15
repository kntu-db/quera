package ir.ac.kntu.web.service;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.model.problem.Submit;
import ir.ac.kntu.web.repository.ProblemRepository;
import ir.ac.kntu.web.service.dto.ProblemDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProblemService {
    List<Problem> search(ProblemRepository.Criteria criteria);

    Problem findById(Integer id);

    Integer create(ProblemDto problemDto);

    void submit(Integer id, MultipartFile file, User user);

    List<Submit> findSubmitsByUserAndProblemId(User user, Integer id);
}
