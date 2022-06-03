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
    }

    public PreparedMapping join(String alias, String path) {
        return this;
    }

    public E getUniqueResult(ResultSet rs) {
        return null;
    }

    public List<E> getResultList(ResultSet rs) {
        try {
            List<E> result = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                E e = entityManager.getEntityMetaInf(entity).getMapper().map(alias, rs);
                for (Join<?> join: joins) {
                    Object joinObj = entityManager.getEntityMetaInf(join.joinClass).getMapper().map(join.alias, rs);
//                    join.setter.invoke(e, joinObj)
                }
                result.add(e);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Could not extract result set", e);
        }
    }

    private static class Join<F> {
        private Class<F> joinClass;
        private String alias;
        private Method setter;

    }
}
