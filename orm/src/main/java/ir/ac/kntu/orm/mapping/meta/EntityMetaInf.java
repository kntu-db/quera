package ir.ac.kntu.orm.mapping.meta;

import ir.ac.kntu.orm.mapping.EntityMapper;
import ir.ac.kntu.orm.mapping.annotations.Key;
import ir.ac.kntu.orm.mapping.annotations.Property;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityMetaInf<E> {
    private final Class<E> entityClass;
    private final EntityMapper<E> entityMapper;
    private final Set<Field> primaryKeyFields;
    private final Set<Field> fields;
    private final Set<String> columns;

    public EntityMetaInf(Class<E> entityClass) {
        this.entityClass = entityClass;
        this.entityMapper = new EntityMapper<>(entityClass);
        fields = Arrays.stream(entityClass.getDeclaredFields()).collect(Collectors.toSet());
        this.primaryKeyFields = fields.stream()
                .filter(field -> field.getAnnotation(Key.class) != null)
                .collect(Collectors.toSet());
        this.columns = fields.stream()
                .filter(field -> field.getAnnotation(Property.class) != null)
                .map(f -> f.getAnnotation(Property.class).column().isBlank() ? f.getName() : f.getAnnotation(Property.class).column())
                .collect(Collectors.toSet());
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public EntityMapper<E> getMapper() {
        return entityMapper;
    }

    public Set<Field> getPrimaryKeyFields() {
        return primaryKeyFields;
    }

    public Set<String> getColumns() {
        return columns;
    }

    public Set<Field> getFields() {
        return fields;
    }
}
