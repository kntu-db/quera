package ir.ac.kntu.web.utils.tx;

public interface TransactionManager {
    void doBegin();
    void doCommit();
    void doRollback();
}
