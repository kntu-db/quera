package ir.ac.kntu.web.model.edu;


import ir.ac.kntu.web.model.problem.ProblemSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class Practice extends ProblemSet {
    private ClassRoom classRoom;
}
