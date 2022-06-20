package ir.ac.kntu.web.repository;

import ir.ac.kntu.web.model.edu.ClassRoom;
import ir.ac.kntu.web.utils.repo.Repository;
import ir.ac.kntu.web.model.problem.ProblemSet;

import java.util.List;
import java.util.Optional;

public interface ProblemSetRepository extends Repository<ProblemSet, Integer> {

    List<Object[]> findByClassRoom(ClassRoom classRoom);

    Optional<ProblemSet> findByIdWithProblems(Integer id);
}
