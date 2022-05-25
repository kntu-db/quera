package ir.ac.kntu.orm.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapper<E> {
    private final Class<E> entity;
    private final ResultSet resultSet;
    private final String alias;

    private EntityMapper<E> mapper;

    public ResultSetMapper(Class<E> entity, String alias, ResultSet rs) {
        this.entity = entity;
        this.resultSet = rs;
        this.alias = alias;
        mapper = EntityMapper.getMapper(entity);
    }

    public List<E> getMappedResultList() throws SQLException {
        List<E> result = new ArrayList<>(resultSet.getFetchSize());

        while (resultSet.next())
            result.add(mapper.map(alias, resultSet));

        return result;
    }

    public E getMappedUniqueResult() throws SQLException {
        E result = null;
        if (resultSet.next())
            result = mapper.map(alias, resultSet);
        if (resultSet.next())
            throw new SQLException("More than one result found");
        return result;
    }
}
