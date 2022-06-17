package ir.ac.kntu.web.model.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<E> {
    void map(ResultSet rs, E sample, String[] commonFields, String alias) throws SQLException;

    E map(ResultSet rs, String[] commonFields, String alias) throws SQLException;

    void map(ResultSet rs, E sample) throws SQLException;

    E map(ResultSet rs) throws SQLException;

}
