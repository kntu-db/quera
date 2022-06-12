package ir.ac.kntu.web.model.auth;

import ir.ac.kntu.web.model.edu.Institute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
public class Developer extends User {
    private Institute institute;
    private Boolean isPublic;

    @Override
    public Collection<Role> getAuthorities() {
        return Collections.emptyList();
    }

}
