package ir.ac.kntu.web.config.data;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
public class FlywayConfiguration {

    @Bean
    public ClassicConfiguration classicConfiguration(FlywayProperties properties, DataSource dataSource) {
        ClassicConfiguration flywayConfiguration = new ClassicConfiguration();
        Location[] locations = properties.getLocations().stream().map(Location::new).toArray(Location[]::new);
        flywayConfiguration.setLocations(locations);
        flywayConfiguration.setDataSource(dataSource);
        return flywayConfiguration;
    }

    @Bean
    public Flyway flyway(ClassicConfiguration configuration) {
        Flyway flyway = new Flyway(configuration);
        flyway.migrate();
        return flyway;
    }

}
