package ir.ac.kntu.web.model.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    EMPLOYING,
    ADMIN,
    ADD_PROBLEM;

    @Override
    public String getAuthority() {
        return name();
    }
}
