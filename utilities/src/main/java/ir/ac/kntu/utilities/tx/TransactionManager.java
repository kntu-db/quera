package ir.ac.kntu.utilities.tx;

public interface TransactionManager {
    void begin();
    void commit();
    void rollback();
}
