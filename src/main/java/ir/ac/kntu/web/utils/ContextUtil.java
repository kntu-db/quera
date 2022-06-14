package ir.ac.kntu.web.utils;

import ir.ac.kntu.web.model.auth.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextUtil {
    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
