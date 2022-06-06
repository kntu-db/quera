package ir.ac.kntu.web.model.problem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProblemSet {
    private Integer id;
    private String title;
    private String description;
    private Date start;
    private Date end;
    private Boolean visibleScores;
    private Boolean visible;
    private Boolean isPublic;
}
