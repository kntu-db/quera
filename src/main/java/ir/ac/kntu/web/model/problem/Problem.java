package ir.ac.kntu.web.model.problem;

import ir.ac.kntu.web.model.mapper.annotations.Property;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Problem {
    @Property private Integer id;
    @Property private ProblemSet problemSet;
    @Property private String title;
    @Property private String text;
    @Property private Integer score;
    @Property private String category;
    private List<String> tags;
}
