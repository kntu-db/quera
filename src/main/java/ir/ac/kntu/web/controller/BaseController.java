package ir.ac.kntu.web.controller;

import ir.ac.kntu.web.service.ProblemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class BaseController {

    private final ProblemService problemService;

    public BaseController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("problems", problemService.search(null));
        return "index";
    }

    @GetMapping("/{id}")
    public String problem(Map<String, Object> model,@PathVariable Integer id) {
        model.put("problem", problemService.findById(id));
        return "problem";
    }

}
