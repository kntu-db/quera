package ir.ac.kntu.web.controller;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.edu.ClassRoom;
import ir.ac.kntu.web.service.ClassService;
import ir.ac.kntu.web.service.ProblemSetService;
import ir.ac.kntu.web.utils.ContextUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/edu")
@Order(0)
public class EduController {

    private final ClassService classService;

    private final ProblemSetService problemSetService;

    public EduController(ClassService classService, ProblemSetService problemSetService) {
        this.classService = classService;
        this.problemSetService = problemSetService;
    }

    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("classes", classService.myClasses(ContextUtil.getUser()));
        model.put("user", ContextUtil.getUser());
        return "edu/index";
    }

    @GetMapping("/{id}")
    public String classRoom(Map<String, Object> model, @PathVariable Integer id) {
        User user = ContextUtil.getUser();
        model.put("user", user);
        ClassRoom byId = classService.findByIdForUser(id, user);
        model.put("classRoom", byId);
        model.put("practices", problemSetService.findByClassRoom(byId));
        return "edu/classRoom";
    }

    @GetMapping("/{id}/practice/{practiceId}")
    public String practice(Map<String, Object> model, @PathVariable Integer id, @PathVariable Integer practiceId) {
        model.put("user", ContextUtil.getUser());
        classService.findByIdForUser(id, ContextUtil.getUser());
        model.put("practice", problemSetService.findByIdWithProblems(practiceId));
        return "edu/practice";
    }
}
