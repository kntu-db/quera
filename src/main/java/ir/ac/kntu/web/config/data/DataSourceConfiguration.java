package ir.ac.kntu.web.config.data;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ir.ac.kntu.web.utils.connection.ConnectionProvider;
import ir.ac.kntu.web.utils.connection.ThreadLocalConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
public class DataSourceConfiguration {

    public DataSource dataSource(C3P0DataSourceProperties dataSourceProps) throws PropertyVetoException {
        ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();

        pooledDataSource.setDriverClass(dataSourceProps.getDriverClassName());
        pooledDataSource.setUser(dataSourceProps.getUsername());
        pooledDataSource.setPassword(dataSourceProps.getPassword());
        pooledDataSource.setJdbcUrl(dataSourceProps.getUrl());
        pooledDataSource.setMaxPoolSize(dataSourceProps.getMaxPoolSize());
        pooledDataSource.setInitialPoolSize(dataSourceProps.getInitialPoolSize());
        pooledDataSource.setMaxIdleTime(dataSourceProps.getMaxIdleTime());
        pooledDataSource.setMinPoolSize(dataSourceProps.getMinPoolSize());
        pooledDataSource.setMaxStatements(dataSourceProps.getMaxStatements());

        return pooledDataSource;
    }

    @Bean
    public ConnectionProvider connectionProvider(C3P0DataSourceProperties dataSourceProperties) throws PropertyVetoException {
        return new ThreadLocalConnectionProvider(dataSource(dataSourceProperties));
    }
}