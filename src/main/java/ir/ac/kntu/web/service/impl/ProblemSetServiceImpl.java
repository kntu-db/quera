package ir.ac.kntu.web.service.impl;

import ir.ac.kntu.web.model.edu.ClassRoom;
import ir.ac.kntu.web.model.problem.ProblemSet;
import ir.ac.kntu.web.repository.ProblemSetRepository;
import ir.ac.kntu.web.service.ProblemSetService;
import ir.ac.kntu.web.service.dto.ProblemSetLightDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemSetServiceImpl implements ProblemSetService {

    private final ProblemSetRepository problemSetRepository;

    public ProblemSetServiceImpl(ProblemSetRepository problemSetRepository) {
        this.problemSetRepository = problemSetRepository;
    }

    @Override
    public List<ProblemSetLightDto> findByClassRoom(ClassRoom classRoom) {
        return problemSetRepository.findByClassRoom(classRoom)
                .stream()
                .map(ps -> new ProblemSetLightDto((ProblemSet) ps[0], (Integer) ps[1]))
                .collect(Collectors.toList());
    }

    @Override
    public ProblemSet findByIdWithProblems(Integer id) {
        return problemSetRepository.findByIdWithProblems(id).orElseThrow(() -> new IllegalArgumentException("No problem set with id " + id));
    }
}
