package ir.ac.kntu.web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class City {
    private Integer id;
    private String name;
    private String state;
}
