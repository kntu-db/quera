package ir.ac.kntu.web.model.edu;

import ir.ac.kntu.web.model.City;
import ir.ac.kntu.web.model.auth.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Institute {
    private Integer id;
    private String name;
    private Type type;
    private City city;
    private String website;
    private User user;

    public enum Type {
        UNIVERSITY,
        SCHOOL,
        INSTITUTE
    }
}
