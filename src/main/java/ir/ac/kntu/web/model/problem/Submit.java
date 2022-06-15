package ir.ac.kntu.web.model.problem;

import ir.ac.kntu.web.model.auth.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

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
    private Integer score;
    private Boolean inContest;
    private Boolean isFinal;

    public boolean isAccepted() {
        return Objects.equals(this.problem.getScore(), this.score);
    }

    public enum Status {
        RECEIVED,
        QUEUED,
        RUNNING,
        JUDGED;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public enum Type {
        GIT, UPLOAD;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
