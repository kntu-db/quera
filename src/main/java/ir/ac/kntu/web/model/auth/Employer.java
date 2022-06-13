package ir.ac.kntu.web.model.auth;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Employer extends User {
    @Override
    public Collection<Role> getAuthorities() {
        return List.of(Role.EMPLOYING);
    }
}
