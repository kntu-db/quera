package ir.ac.kntu.web.service.impl;

import ir.ac.kntu.web.exceptions.UserAlreadyExistException;
import ir.ac.kntu.web.model.auth.Developer;
import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.repository.UserRepository;
import ir.ac.kntu.web.service.UserService;
import ir.ac.kntu.web.service.dto.UserDto;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (userRepository.emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userDto.getEmail());
        }

        Developer d = new Developer();
        d.setFirstname(userDto.getName());
        d.setLastname(userDto.getFamily());
        d.setPassword(userDto.getPassword());
        d.setMail(userDto.getEmail());
        d.setJoinedAt(new Date());
        d.setStatus(User.Status.ACTIVE);
        d.setIsPublic(true);

        User user = userRepository.save(d);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Optional<User> byMail = userRepository.findByMailWithAuthorities(username);
        if (byMail.isPresent()) {
            return byMail.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

}
