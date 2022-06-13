package ir.ac.kntu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/auth")
    public String auth() {
        return "auth";
    }
}
