package ir.ac.kntu.web.model.auth;

import ir.ac.kntu.web.model.edu.Institute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Developer extends User {
    private Institute institute;
    private Boolean isPublic;
}
