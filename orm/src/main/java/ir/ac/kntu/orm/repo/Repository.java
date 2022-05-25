package ir.ac.kntu.orm.repo;

import ir.ac.kntu.orm.repo.annotations.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<Entity> {
    public Entity save(Entity entity);
    public void delete(Entity entity);
    public void update(Entity entity);
}
