package ir.ac.kntu.web.service.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProblemCriteria {
    private String query;
    private List<String> tags;
}
