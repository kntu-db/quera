package ir.ac.kntu.web.model.problem;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Contest extends ProblemSet {
    private String sponsor;
    private Boolean vip;
}
