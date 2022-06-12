package ir.ac.kntu.web.model.magnet;

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
public class JobOffer {
    private Integer id;
    private Date createdAt;
    private Level level;
    private Cooperation cooperation;
    private Boolean workDistance;
    private Integer salary;
    private String title;
    private Company company;
    private City city;
    private String description;
    private Boolean expired;

    public enum Cooperation {
        PART_TIME, FULL_TIME
    }

    public enum Level {
        INTERN,
        JUNIOR,
        MIDDLE,
        SENIOR,
        LEAD
    }
}
