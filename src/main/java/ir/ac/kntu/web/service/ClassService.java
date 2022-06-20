package ir.ac.kntu.web.service;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.edu.ClassRoom;

import java.util.List;

public interface ClassService {
    List<ClassRoom> myClasses(User user);

    ClassRoom findByIdForUser(Integer id, User user);
}
