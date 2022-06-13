package ir.ac.kntu.web.service;

import ir.ac.kntu.web.exceptions.UserAlreadyExistException;
import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.service.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException;
}
