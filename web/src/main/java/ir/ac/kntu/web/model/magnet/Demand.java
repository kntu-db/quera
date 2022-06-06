package ir.ac.kntu.web.model.magnet;

import ir.ac.kntu.web.model.auth.Developer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Demand {
    private Developer developer;
    private JobOffer jobOffer;
    private String description;
    private Date time;
    private String cvURI;
    private DemandStatus status;
}
