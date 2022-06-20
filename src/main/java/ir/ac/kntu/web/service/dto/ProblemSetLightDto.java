package ir.ac.kntu.web.service.dto;

import ir.ac.kntu.web.model.problem.ProblemSet;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProblemSetLightDto {
    private Integer id;
    private String title;
    private String description;
    private Date start;
    private Date end;
    private Integer problemsCount;

    public ProblemSetLightDto(ProblemSet problemSet, Integer problemsCount) {
        this.id = problemSet.getId();
        this.title = problemSet.getTitle();
        this.description = problemSet.getDescription();
        this.start = problemSet.getStart();
        this.end = problemSet.getEnd();
        this.problemsCount = problemsCount;
    }

    public Integer getRemainingDays() {
        return Math.max((int) ((end.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24)), 0);
    }
}
