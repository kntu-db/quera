package ir.ac.kntu.web.config.data;

import ir.ac.kntu.orm.mapping.meta.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ORMConfiguration {

    @Bean
    public EntityManager entityManager(DataSource dataSource) {
        return new EntityManager(dataSource);
    }

}
