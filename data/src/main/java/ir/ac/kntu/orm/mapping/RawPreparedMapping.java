package ir.ac.kntu.orm.mapping;

import ir.ac.kntu.orm.mapping.meta.EntityManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RawPreparedMapping {

    public RawPreparedMapping() {
    }

    public List<Object[]> getResultList(ResultSet rs) {
        try {
            List<Object[]> result = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                Object[] e = getObjects(rs);
                result.add(e);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getObjects(ResultSet rs) throws SQLException {
        Object[] e = new Object[rs.getMetaData().getColumnCount()];
        for (int i = 1; i <= e.length; i++)
            e[i - 1] = rs.getObject(i);
        return e;
    }

    public Object[] getUniqueResult(ResultSet rs) {
        Object[] result = null;
        try {
            if (rs.next()) {
                result = getObjects(rs);
            }
            if (rs.next())
                throw new SQLException("More than one result found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
