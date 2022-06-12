package ir.ac.kntu.web.model.magnet;

import ir.ac.kntu.web.model.City;
import ir.ac.kntu.web.model.auth.Employer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Company {
    private int id;
    private String name;
    private Date foundedDate;
    private String website;
    private String description;
    private String logo;
    private Employer employer;
    private String address;
    private City city;
    private Integer size;
    private String field;
}
