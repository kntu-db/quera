package ir.ac.kntu.web.utils.connection;

import javax.sql.DataSource;

public interface ConnectionProvider extends DataSource {
    void bind();
    void unbind();

}
