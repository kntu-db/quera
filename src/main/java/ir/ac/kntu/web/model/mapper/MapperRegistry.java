package ir.ac.kntu.web.model.mapper;

import ir.ac.kntu.web.model.problem.Problem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperRegistry {
    @Bean
    public Mapper<Problem> problemMapper() {
        return MapperFactory.forEntity(Problem.class);
    }
}
