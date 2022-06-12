package ir.ac.kntu.web.utils.tx;

public interface TransactionManager {
    void begin();
    void commit();
    void rollback();
}
