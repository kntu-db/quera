package ir.ac.kntu.utilities.connection;

import javax.sql.DataSource;

public interface ConnectionProvider extends DataSource {
    void bind();
    void unbind();

}
