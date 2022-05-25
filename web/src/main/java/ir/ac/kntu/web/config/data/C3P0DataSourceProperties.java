package ir.ac.kntu.web.config.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.datasource.c3p0")
public class C3P0DataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialPoolSize;
    private int maxIdleTime;
    private int maxPoolSize;
    private int minPoolSize;
    private int maxStatements;
}
