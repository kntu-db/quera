package ir.ac.kntu.web.model.auth;


import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Setter
@NoArgsConstructor
public class Manager extends User {

    Collection<Role> authorities;

    @Override
    public Collection<Role> getAuthorities() {
        return authorities;
    }

}
