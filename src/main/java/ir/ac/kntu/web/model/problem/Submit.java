package ir.ac.kntu.web.model.problem;

import ir.ac.kntu.web.model.auth.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Submit {
    private Integer id;
    private Problem problem;
    private User user;
    private Date time;
    private Status status;
    private String uri;
    private Type type;
    private String score;
    private Boolean inContest;
    private Boolean isFinal;

    public enum Status {
        RECEIVED,
        QUEUED,
        RUNNING,
        JUDGED,
    }

    public enum Type {
        GIT, UPLOAD
    }
}
