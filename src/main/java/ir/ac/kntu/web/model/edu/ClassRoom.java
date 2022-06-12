package ir.ac.kntu.web.model.edu;

import ir.ac.kntu.web.model.auth.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClassRoom {
    private Integer id;
    private String title;
    private String professor;
    private String description;
    private String phone;
    private String password;
    private Integer maxCount;
    private Boolean archived;
    private Boolean openToRegister;
    private Institute institute;
    private User creator;
    private Integer year;
    private Turn turn;
    private Boolean publishAfterArchive;

    public enum Turn {
        FALL, SPRING, SUMMER
    }
}
