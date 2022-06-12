package ir.ac.kntu.web.utils.tx;

import ir.ac.kntu.web.utils.connection.ConnectionProvider;

public class DefaultTransactionManager implements TransactionManager {

    private final ConnectionProvider provider;

    public DefaultTransactionManager(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void begin() {
        try {
            provider.getConnection().setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException("Cannot obtain connection", e);
        }

    }

    @Override
    public void commit() {
        try {
            provider.getConnection().commit();
            provider.getConnection().setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException("Cannot commit transaction", e);
        }
    }

    @Override
    public void rollback() {
        try {
            provider.getConnection().rollback();
            provider.getConnection().setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException("Cannot rollback transaction", e);
        }
    }
}
