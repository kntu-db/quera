package ir.ac.kntu.web.utils.tx;

import ir.ac.kntu.web.utils.connection.ConnectionProvider;
import org.springframework.stereotype.Component;

@Component
public class DefaultTransactionManager implements TransactionManager {

    private final ConnectionProvider provider;

    public DefaultTransactionManager(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void doBegin() {
        try {
            provider.getConnection().setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException("Cannot obtain connection", e);
        }
    }

    @Override
    public void doCommit() {
        try {
            provider.getConnection().commit();
            provider.getConnection().setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException("Cannot commit transaction", e);
        }
    }

    @Override
    public void doRollback() {
        try {
            provider.getConnection().rollback();
            provider.getConnection().setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException("Cannot rollback transaction", e);
        }
    }
}
