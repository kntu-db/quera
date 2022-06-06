package ir.ac.kntu.web.model.problem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Problem {
    private Integer id;
    private Integer number;
    private ProblemSet problemSet;
    private String title;
    private String text;
    private Integer score;
    private String category;
}
