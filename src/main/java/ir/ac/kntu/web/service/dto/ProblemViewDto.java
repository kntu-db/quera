package ir.ac.kntu.web.service.dto;

import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.model.problem.ProblemSet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProblemViewDto {
    private Integer id;
    private String title;
    private String text;
    private Integer score;
    private String category;
    private List<String> tags;
    private Integer solveCount;

    public ProblemViewDto(Problem problem, Integer solveCount) {
        this.id = problem.getId();
        this.title = problem.getTitle();
        this.text = problem.getText();
        this.score = problem.getScore();
        this.category = problem.getCategory();
        this.tags = problem.getTags();
        this.solveCount = solveCount;
    }
}
