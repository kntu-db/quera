package ir.ac.kntu.web.service.impl;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.model.edu.ClassRoom;
import ir.ac.kntu.web.repository.ClassRoomRepository;
import ir.ac.kntu.web.service.ClassService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRoomRepository classRoomRepository;

    public ClassServiceImpl(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    @Override
    public List<ClassRoom> myClasses(User user) {
        return classRoomRepository.findByUser(user);
    }

    @Override
    public ClassRoom findByIdForUser(Integer id, User user) {
        ClassRoom classRoom = classRoomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Class not found"));
        if (!classRoomRepository.isUserInClass(classRoom, user)) {
            throw new IllegalArgumentException("You are not authorized to access this class");
        }
        return classRoom;
    }
}
