package ir.ac.kntu.orm.mapping;

import ir.ac.kntu.orm.mapping.annotations.Property;
import ir.ac.kntu.orm.utils.NameConverter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityMapper<E> {
    private final Class<E> clazz;

    private final Map<Method, String> setters;

    public EntityMapper(Class<E> clazz) {
        this.clazz = clazz;
        setters = new HashMap<>(clazz.getDeclaredMethods().length);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Property.class) != null) {
                String preferredName = field.getAnnotation(Property.class).column();
                String name = preferredName.isBlank() ? field.getName() : preferredName;
                try {
                    Method setter = clazz.getDeclaredMethod(NameConverter.fieldToSetter(name), field.getType());
                    setters.put(setter, name);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("No setter found for field " + name);
                }
            }
        }
    }

    public E map(String alias, ResultSet rs) throws SQLException {
        try {
            E entity = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<Method, String> entry : setters.entrySet()) {
                Method setter = entry.getKey();
                String name = entry.getValue();
                Object object = rs.getObject(name);
                setter.invoke(entity, object);
            }
            return entity;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No constructor found for class " + clazz.getName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Constructors and setters must be public");
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot instantiate class " + clazz.getName());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot invoke constructor of class " + clazz.getName());
        }
    }
}
