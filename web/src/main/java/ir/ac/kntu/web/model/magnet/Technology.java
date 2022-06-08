package ir.ac.kntu.web.model.magnet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Technology {
    private Integer id;
    private String title;
    private String category;
}
