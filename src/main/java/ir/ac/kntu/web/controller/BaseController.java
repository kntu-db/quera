package ir.ac.kntu.web.controller;

import ir.ac.kntu.web.model.auth.Role;
import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.problem.Problem;
import ir.ac.kntu.web.repository.ProblemSetRepository;
import ir.ac.kntu.web.service.ProblemService;
import ir.ac.kntu.web.service.builder.ProblemCriteria;
import ir.ac.kntu.web.service.dto.ProblemDto;
import ir.ac.kntu.web.utils.ContextUtil;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Order(1)
public class BaseController {

    private final ProblemService problemService;

    private final ProblemSetRepository problemSetRepository;

    public BaseController(ProblemService problemService, ProblemSetRepository problemSetRepository) {
        this.problemService = problemService;
        this.problemSetRepository = problemSetRepository;
    }

    @GetMapping
    public String index(Map<String, Object> model, ProblemCriteria criteria) {
        User user = ContextUtil.getUser();
        model.put("canAddProblem", user.getAuthorities().contains(Role.ADD_PROBLEM));
        model.put("problems", problemService.search(criteria, ContextUtil.getUser()));
        model.put("user", user);
        model.put("tags", problemService.findAllTags());
        return "index";
    }

    @GetMapping("/{id}")
    public String problem(Map<String, Object> model, @PathVariable Integer id) {
        model.put("user", ContextUtil.getUser());
        model.put("problem", problemService.findById(id));
        return "problem";
    }

    @PostMapping("/{id}/submit")
    public String submit(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        problemService.submit(id, file, ContextUtil.getUser());
        return String.format("redirect:/%d/submits", id);
    }

    @GetMapping("/{id}/submits")
    public String submits(Map<String, Object> model, @PathVariable Integer id) {
        model.put("user", ContextUtil.getUser());
        model.put("problemId", id);
        model.put("submits", problemService.findSubmitsByUserAndProblemId(ContextUtil.getUser(), id));
        return "submits";
    }

    @Secured("ADD_PROBLEM")
    @GetMapping("/create")
    public String newProblem(Map<String, Object> model) {
        model.put("user", ContextUtil.getUser());
        model.put("problem", new ProblemDto());
        return "newProblem";
    }

    @Secured("ADD_PROBLEM")
    @PostMapping("/create")
    public String createProblem(@ModelAttribute("problem") @Valid ProblemDto problemDto) {
        Integer id = problemService.create(problemDto);
        return "redirect:/" + id;
    }

    @GetMapping("/profile")
    public String profile(Map<String, Object> model) {
        Map<String, List<Problem>> solvedProblems = problemService.solved(ContextUtil.getUser()).stream().collect(Collectors.groupingBy(Problem::getCategory));
        model.put("user", ContextUtil.getUser());
        model.put("solvedProblems", solvedProblems);
        return "profile";
    }

}
