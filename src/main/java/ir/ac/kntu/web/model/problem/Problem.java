package ir.ac.kntu.web.model.problem;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Problem {
    private Integer id;
    private Integer number;
    private ProblemSet problemSet;
    private String title;
    private String text;
    private Integer score;
    private String category;
    private List<String> tags;
}
