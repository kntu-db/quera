package ir.ac.kntu.web.model.problem;

import ir.ac.kntu.web.model.mapper.annotations.Property;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public abstract class ProblemSet {
    @Property private Integer id;
    @Property private String title;
    @Property private String description;
    @Property private Date start;
    @Property private Date end;
    @Property private Boolean visibleScores;
    @Property private Boolean visible;
    @Property("public") private Boolean isPublic;
    private List<Problem> problems;
}
