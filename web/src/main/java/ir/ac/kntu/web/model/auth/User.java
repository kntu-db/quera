package ir.ac.kntu.web.model.auth;

import ir.ac.kntu.web.model.City;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    private Integer id;
    private String firstname;
    private String lastname;
    private String mail;
    private String password;
    private String phone;
    private String status;
    private City city;
    private Date joinedAt;
    private Date birthDate;
}
