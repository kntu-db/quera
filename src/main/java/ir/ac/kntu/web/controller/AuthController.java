package ir.ac.kntu.web.controller;

import ir.ac.kntu.web.exceptions.UserAlreadyExistException;
import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.service.UserService;
import ir.ac.kntu.web.service.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String auth() {
        return "auth";
    }

    @PostMapping("/register")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto,
            HttpServletRequest request,
            Errors errors) {

        try {
            User registered = userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException uaeEx) {
            errors.rejectValue("email", "user.already.exists", "User already exists");
            return "auth";
        }

        return "redirect:/";
    }
}
