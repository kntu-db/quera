package ir.ac.kntu.orm.repo;

import javassist.ClassPool;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class RepositoryHandler implements InvocationHandler {

    public RepositoryHandler(Class<?> repositoryClass) {
        Arrays.stream(repositoryClass.getMethods()).forEach(System.out::println);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}

public class RepositoryFactory {

    private final ClassPool pool = ClassPool.getDefault();

    public RepositoryFactory() {
    }

    @SuppressWarnings("unchecked")
    public <R extends Repository> R createRepository(Class<? extends R> repositoryClass) {
        return (R) Proxy.newProxyInstance(
                repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                new RepositoryHandler(repositoryClass)
        );
    }

}
