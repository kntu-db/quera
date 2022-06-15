package ir.ac.kntu.web.controller;

import ir.ac.kntu.web.service.ProblemService;
import ir.ac.kntu.web.service.dto.ProblemDto;
import ir.ac.kntu.web.utils.ContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class BaseController {

    private final ProblemService problemService;

    public BaseController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public String index(Map<String, Object> model) {
        System.out.println(ContextUtil.getUser());
        System.out.println(ContextUtil.getUser().getAuthorities());
        model.put("problems", problemService.search(null));
        return "index";
    }

    @GetMapping("/{id}")
    public String problem(Map<String, Object> model,@PathVariable Integer id) {
        model.put("problem", problemService.findById(id));
        return "problem";
    }

    @GetMapping("/create")
    public String newProblem(Map<String, Object> model) {
        model.put("problem", new ProblemDto());
        return "newProblem";
    }

    @PostMapping("/create")
    public String createProblem(@ModelAttribute("problem") @Valid ProblemDto problemDto) {
        Integer id = problemService.create(problemDto);
        return "redirect:/" + id;
    }

}
