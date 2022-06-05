package ir.ac.kntu.orm.tx;

import ir.ac.kntu.orm.tx.annotations.Transactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

class ServiceHandler implements InvocationHandler {

    Connection connection;

//    public ServiceHandler(Connection connection) throws SQLException {
//        this.connection = connection;
//        connection.setAutoCommit(false);
//    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Transactional tx = method.getAnnotation(Transactional.class);
        if (tx == null || tx.readOnly())
            return method.invoke(proxy, args);
        // handle transactional method
        return null;
    }

}

public class ServiceFactory {

    @SuppressWarnings("unchecked")
    public static <S> S getService(Class<? extends S> serviceClass) {
        return (S) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceHandler()
        );
    }

}
