package ir.ac.kntu.web.repository;

import ir.ac.kntu.web.model.auth.User;
import ir.ac.kntu.web.utils.repo.Repository;
import ir.ac.kntu.web.model.edu.ClassRoom;

import java.util.List;

public interface ClassRoomRepository extends Repository<ClassRoom, Integer> {

    List<ClassRoom> findByUser(User user);

    boolean isUserInClass(ClassRoom classRoom, User user);
}
