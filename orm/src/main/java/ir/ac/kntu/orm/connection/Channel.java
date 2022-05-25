package ir.ac.kntu.orm.connection;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class Channel {

    DataSource dataSource;

    public Channel(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    
//    public void persist(Object entity) {
//
//    }
//
//
//    public <T> T merge(T entity) {
//        return null;
//    }
//
//
//    public void remove(Object entity) {
//
//    }
//
//
//    public <T> T find(Class<T> entityClass, Object primaryKey) {
//        return null;
//    }
//
//
//    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
//        return null;
//    }
//
//
//    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
//        return null;
//    }
//
//
//    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
//        return null;
//    }
//
//
//    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
//        return null;
//    }
//
//
//    public void flush() {
//
//    }
//
//
//    public void setFlushMode(FlushModeType flushMode) {
//
//    }
//
//
//    public FlushModeType getFlushMode() {
//        return null;
//    }
//
//
//    public void lock(Object entity, LockModeType lockMode) {
//
//    }
//
//
//    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
//
//    }
//
//
//    public void refresh(Object entity) {
//
//    }
//
//
//    public void refresh(Object entity, Map<String, Object> properties) {
//
//    }
//
//
//    public void refresh(Object entity, LockModeType lockMode) {
//
//    }
//
//
//    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
//
//    }
//
//
//    public void clear() {
//
//    }
//
//
//    public void detach(Object entity) {
//
//    }
//
//
//    public boolean contains(Object entity) {
//        return false;
//    }
//
//
//    public LockModeType getLockMode(Object entity) {
//        return null;
//    }
//
//
//    public void setProperty(String propertyName, Object value) {
//
//    }
//
//
//    public Map<String, Object> getProperties() {
//        return null;
//    }
//
//
//    public Query createQuery(String qlString) {
//        return null;
//    }
//
//
//    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
//        return null;
//    }
//
//
//    public Query createQuery(CriteriaUpdate updateQuery) {
//        return null;
//    }
//
//
//    public Query createQuery(CriteriaDelete deleteQuery) {
//        return null;
//    }
//
//
//    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
//        return null;
//    }
//
//
//    public Query createNamedQuery(String name) {
//        return null;
//    }
//
//
//    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
//        return null;
//    }
//
//
//    public Query createNativeQuery(String sqlString) {
//        return null;
//    }
//
//
//    public Query createNativeQuery(String sqlString, Class resultClass) {
//        return null;
//    }
//
//
//    public Query createNativeQuery(String sqlString, String resultSetMapping) {
//        return null;
//    }
//
//
//    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
//        return null;
//    }
//
//
//    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
//        return null;
//    }
//
//
//    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
//        return null;
//    }
//
//
//    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
//        return null;
//    }
//
//
//    public void joinTransaction() {
//
//    }
//
//
//    public boolean isJoinedToTransaction() {
//        return false;
//    }
//
//
//    public <T> T unwrap(Class<T> cls) {
//        return null;
//    }
//
//
//    public Object getDelegate() {
//        return null;
//    }
//
//
//    public void close() {
//
//    }
//
//
//    public boolean isOpen() {
//        return false;
//    }
//
//
//    public EntityTransaction getTransaction() {
//        return null;
//    }
//
//
//    public EntityManagerFactory getEntityManagerFactory() {
//        return null;
//    }
//
//
//    public CriteriaBuilder getCriteriaBuilder() {
//        return null;
//    }
//
//
//    public Metamodel getMetamodel() {
//        return null;
//    }
//
//
//    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
//        return null;
//    }
//
//
//    public EntityGraph<?> createEntityGraph(String graphName) {
//        return null;
//    }
//
//
//    public EntityGraph<?> getEntityGraph(String graphName) {
//        return null;
//    }
//
//
//    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
//        return null;
//    }
}
