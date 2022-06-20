package ir.ac.kntu.web.service;

import ir.ac.kntu.web.model.edu.ClassRoom;
import ir.ac.kntu.web.model.edu.Practice;
import ir.ac.kntu.web.model.problem.ProblemSet;
import ir.ac.kntu.web.service.dto.ProblemSetLightDto;

import java.util.List;

public interface ProblemSetService
{
    List<ProblemSetLightDto> findByClassRoom(ClassRoom classRoom);

    ProblemSet findByIdWithProblems(Integer id);
}
