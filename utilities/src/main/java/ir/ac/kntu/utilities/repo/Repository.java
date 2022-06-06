package ir.ac.kntu.utilities.repo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<Entity, ID extends Serializable> {

    Optional<Entity> findById(ID id);

    List<Entity> findAll();

    Entity save(Entity entity);

    void update(Entity entity);

    void delete(Entity entity);

}