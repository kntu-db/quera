package ir.ac.kntu.web.model.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    EMPLOYING;

    @Override
    public String getAuthority() {
        return name();
    }
}
