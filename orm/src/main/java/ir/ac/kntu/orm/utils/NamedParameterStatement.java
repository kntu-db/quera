package ir.ac.kntu.orm.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NamedParameterStatement {

    private PreparedStatement prepStmt;
    private List<String> fields = new ArrayList<String>();

    public NamedParameterStatement(Connection conn, String sql) throws SQLException {
        int pos;
        while ((pos = sql.indexOf(":")) != -1) {
            int end = sql.substring(pos).indexOf(" ");
            if (end == -1)
                end = sql.length();
            else
                end += pos;
            fields.add(sql.substring(pos + 1, end));
            sql = sql.substring(0, pos) + "?" + sql.substring(end);
        }
        prepStmt = conn.prepareStatement(sql);
    }

    public PreparedStatement getPreparedStatement() {
        return prepStmt;
    }

    public ResultSet executeQuery() throws SQLException {
        return prepStmt.executeQuery();
    }

    public void close() throws SQLException {
        prepStmt.close();
    }

    public NamedParameterStatement setInt(String name, int value) throws SQLException {
        prepStmt.setInt(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setString(String name, String value) throws SQLException {
        prepStmt.setString(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setLong(String name, long value) throws SQLException {
        prepStmt.setLong(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setDouble(String name, double value) throws SQLException {
        prepStmt.setDouble(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setFloat(String name, float value) throws SQLException {
        prepStmt.setFloat(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setBoolean(String name, boolean value) throws SQLException {
        prepStmt.setBoolean(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setByte(String name, byte value) throws SQLException {
        prepStmt.setByte(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setShort(String name, short value) throws SQLException {
        prepStmt.setShort(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setBytes(String name, byte[] value) throws SQLException {
        prepStmt.setBytes(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setDate(String name, java.sql.Date value) throws SQLException {
        prepStmt.setDate(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setTime(String name, java.sql.Time value) throws SQLException {
        prepStmt.setTime(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setTimestamp(String name, java.sql.Timestamp value) throws SQLException {
        prepStmt.setTimestamp(getIndex(name), value);
        return this;
    }

    public NamedParameterStatement setNull(String name, int sqlType) throws SQLException {
        prepStmt.setNull(getIndex(name), sqlType);
        return this;
    }

    public NamedParameterStatement setNull(String name, int sqlType, String typeName) throws SQLException {
        prepStmt.setNull(getIndex(name), sqlType, typeName);
        return this;
    }

    public NamedParameterStatement setObject(String name, Object value, int sqlType) throws SQLException {
        prepStmt.setObject(getIndex(name), value, sqlType);
        return this;
    }

    public NamedParameterStatement setObject(String name, Object value, int sqlType, int scale) throws SQLException {
        prepStmt.setObject(getIndex(name), value, sqlType, scale);
        return this;
    }

    public NamedParameterStatement setObject(String name, Object value) throws SQLException {
        prepStmt.setObject(getIndex(name), value);
        return this;
    }

    public int executeUpdate() throws SQLException {
        return prepStmt.executeUpdate();
    }

    private int getIndex(String name) {
        return fields.indexOf(name) + 1;
    }
}
