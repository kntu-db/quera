package ir.ac.kntu.web.service.impl;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.repository.UserRepository;
import ir.ac.kntu.web.service.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byMail = userRepository.findByMailWithAuthorities(username);
        if (byMail.isPresent()) {
            return byMail.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

}
