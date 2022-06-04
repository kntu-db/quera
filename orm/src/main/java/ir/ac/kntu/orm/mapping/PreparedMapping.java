package ir.ac.kntu.orm.mapping;

import ir.ac.kntu.orm.mapping.meta.EntityManager;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreparedMapping<E> {

    private final EntityManager entityManager;
    private final Class<E> entity;
    private final String alias;
    public List<Join> joins;

    public PreparedMapping(EntityManager manager, Class<E> entity, String alias) {
        this.entityManager = manager;
        this.entity = entity;
        this.alias = alias;
        this.joins = new ArrayList<>();
    }

    public PreparedMapping join(String alias, String path) {
        return this;
    }

    public List<E> getResultList(ResultSet rs) {
        try {
            List<E> result = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                E e = entityManager.getEntityMetaInf(entity).getMapper().map(alias, rs);
                result.add(e);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public E getUniqueResult(ResultSet rs) {
        try {
            E result = null;
            if (rs.next())
                result = entityManager.getEntityMetaInf(entity).getMapper().map(alias, rs);
            if (rs.next())
                throw new SQLException("More than one result found");
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Join<F> {
        private Class<F> joinClass;
        private String alias;
        private Method setter;

    }
}
