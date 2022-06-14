package ir.ac.kntu.web.service.dto;

import ir.ac.kntu.web.model.problem.ProblemSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ProblemDto {
    @NotNull
    @NotEmpty
    private String title;
    @NotNull
    @NotEmpty
    private String text;
    @NotNull
    private Integer score;
    @NotNull
    @NotEmpty
    private String category;
    private String tags;
}
