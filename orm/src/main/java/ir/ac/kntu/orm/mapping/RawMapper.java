package ir.ac.kntu.orm.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Converts result set to Object[] or List<Object[]>
 */
public class RawMapper {

    private final ResultSet rs;

    public RawMapper(ResultSet rs) {
        this.rs = rs;
    }

    public Object[] getMappedUniqueResult() throws SQLException {
        Object[] result = null;
        if (rs.next()) {
            result = new Object[rs.getMetaData().getColumnCount()];
            for (int i = 0; i < result.length; i++)
                result[i] = rs.getObject(i + 1);
        }
        if (rs.next())
            throw new SQLException("Result set contains more than one row");
        return result;
    }

    public List<Object[]> getMappedResultList() throws SQLException {
        List<Object[]> result = null;
        while (rs.next()) {
            if (result == null)
                result = new java.util.ArrayList<>();
            Object[] row = new Object[rs.getMetaData().getColumnCount()];
            for (int i = 0; i < row.length; i++)
                row[i] = rs.getObject(i + 1);
            result.add(row);
        }
        return result;
    }
}
