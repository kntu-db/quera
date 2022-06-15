package ir.ac.kntu.web.model.auth;

import ir.ac.kntu.web.model.City;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public abstract class User implements UserDetails {
    private Integer id;
    private String firstname;
    private String lastname;
    private String mail;
    private String password;
    private String phone;
    private Status status;
    private City city;
    private Date joinedAt;
    private Date birthDate;

    @Override
    public boolean isEnabled() {
        return this.getStatus().equals(Status.ACTIVE);
    }

    @Override
    public String getUsername() {
        return this.getMail();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.getStatus().equals(Status.INACTIVE);
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.getStatus().equals(Status.NOT_VERIFIED);
    }

    public String getFullName() {
        return this.getFirstname() + " " + this.getLastname();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.getStatus().equals(Status.NOT_VERIFIED);
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
        NOT_VERIFIED
    }

}
