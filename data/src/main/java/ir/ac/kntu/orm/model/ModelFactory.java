package ir.ac.kntu.orm.model;

import ir.ac.kntu.orm.mapping.annotations.Relation;
import ir.ac.kntu.orm.utils.NameConverter;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class GetterFilter implements MethodFilter {

    private static final Set<Class<?>> primitives = Set.of(
            Integer.class,
            Long.class,
            Double.class,
            Float.class,
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class
    );

    private Set<String> relations;

    private void validate(Class<?> clazz, Set<Field> relations) {
        // Check getters and setters
        relations.forEach(field -> {
            if (primitives.contains(field.getType())) throw new IllegalArgumentException(
                    "Relation field " + field.getName() + " of class " + clazz.getName() + " is primitive"
            );
            try {
                clazz.getDeclaredMethod(NameConverter.fieldToGetter(field.getName()));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No getter for field " + field.getName() + " in class " + clazz.getName());
            }
            try {
                clazz.getDeclaredMethod(NameConverter.fieldToSetter(field.getName()), field.getType());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No setter for field " + field.getName() + " in class " + clazz.getName());
            }
        });
    }

    public GetterFilter(Class<?> clazz) {
        Set<Field> relations = Arrays
                .stream(clazz.getDeclaredFields())
                .filter(f -> f.getAnnotation(Relation.class) != null)
                .collect(Collectors.toSet());
        validate(clazz, relations);

        this.relations = new HashSet<>();

        relations.forEach(f -> this.relations.add(NameConverter.fieldToSetter(f.getName())));
        relations.forEach(f -> this.relations.add(NameConverter.fieldToGetter(f.getName())));
    }

    @Override
    public boolean isHandled(Method m) {
        return relations.contains(m.getName());
    }

}

class LazyLoader implements MethodHandler {

    private Set<String> resolved;

    public LazyLoader() {
        resolved = new HashSet<>(0);
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        if (thisMethod.getName().startsWith("get")) {
            if (!resolved.contains(thisMethod.getName())) {
                // print all declared methods
                Method setter = self.getClass().getDeclaredMethod(NameConverter.getterToSetter(thisMethod.getName()), thisMethod.getReturnType());
                setter.invoke(self, Arrays.asList());
                resolved.add(thisMethod.getName());
            }
        }
        return proceed.invoke(self, args);
    }
}

public class ModelFactory<E> {

    private ProxyFactory proxyFactory;

    public static <E> ModelFactory<E> getFactory(Class<E> clazz) {
        ModelFactory<E> factory = new ModelFactory<>();
        factory.proxyFactory = new ProxyFactory();
        factory.proxyFactory.setSuperclass(clazz);
        factory.proxyFactory.setFilter(new GetterFilter(clazz));
        return factory;
    }

    @SuppressWarnings("unchecked")
    public E create() throws Throwable {
        return (E) proxyFactory.create(new Class<?>[0], new Object[0], new LazyLoader());
    }

}
