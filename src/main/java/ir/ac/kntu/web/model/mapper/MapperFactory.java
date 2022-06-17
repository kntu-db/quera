package ir.ac.kntu.web.model.mapper;

import ir.ac.kntu.web.model.mapper.annotations.Property;
import ir.ac.kntu.web.utils.NameConverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MapperFactory {

    public static <E> Mapper<E> forEntity(Class<E> clazz) {
        return new AbstractMapper<>(clazz);
    }

    private static class AbstractMapper<E> implements Mapper<E> {

        private Class<E> clazz;

        private Map<String, Method> setters;

        private Constructor<E> constructor;

        private AbstractMapper(Class<E> clazz) {
            this.clazz = clazz;
            setters = Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> f.getAnnotation(Property.class) != null)
                    .collect(Collectors.toMap(f -> {
                        String preferredName = f.getAnnotation(Property.class).value();
                        return preferredName.isBlank() ? f.getName() : preferredName;
                    }, f -> {
                        try {
                            return clazz.getMethod(NameConverter.fieldToSetter(f.getName()), f.getType());
                        } catch (NoSuchMethodException ex) {
                            throw new RuntimeException("No setter found for property " + f.getName());
                        }
                    }));
            try {
                this.constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException("No 0 args constructor found for entity " + clazz.getName());
            }
        }

        @Override
        public void map(ResultSet rs, E sample, String[] commonFields, String alias) throws SQLException {
            Set<String> commons = Set.of(commonFields);
            for (Map.Entry<String, Method> entry: setters.entrySet()) {
                Object obj = rs.getObject(commons.contains(entry.getKey()) ? String.format("%s.%s", alias, entry.getKey()) : entry.getKey());
                try {
                    entry.getValue().invoke(sample, obj);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot access setter of field " + NameConverter.setterToField(entry.getValue().getName()));
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Cannot invoke setter of field " + NameConverter.setterToField(entry.getValue().getName()));
                }
            }
        }

        @Override
        public E map(ResultSet rs, String[] commonFields, String alias) throws SQLException {
            E sample = newInstance();
            map(rs, sample, commonFields, alias);
            return sample;
        }

        @Override
        public void map(ResultSet rs, E sample) throws SQLException {
            map(rs, sample, new String[0], "");
        }

        @Override
        public E map(ResultSet rs) throws SQLException {
            E sample = newInstance();
            map(rs, sample, new String[0], "");
            return sample;
        }

        private E newInstance() {
            try {
                return this.constructor.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot instantiate entity " + clazz.getName());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot access constructor of entity " + clazz.getName());
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot invoke constructor of entity " + clazz.getName());
            }
        }
    }
}
