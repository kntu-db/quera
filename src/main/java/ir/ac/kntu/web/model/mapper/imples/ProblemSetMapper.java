package ir.ac.kntu.web.model.mapper.imples;

import ir.ac.kntu.web.model.edu.Practice;
import ir.ac.kntu.web.model.mapper.Mapper;
import ir.ac.kntu.web.model.mapper.MapperFactory;
import ir.ac.kntu.web.model.problem.Contest;
import ir.ac.kntu.web.model.problem.ProblemSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProblemSetMapper implements Mapper<ProblemSet> {

    private final Mapper<ProblemSet> handler = MapperFactory.forEntity(ProblemSet.class);

    @Override
    public void map(ResultSet rs, ProblemSet sample, String[] commonFields, String alias) throws SQLException {
        handler.map(rs, sample, commonFields, alias);
    }

    @Override
    public ProblemSet map(ResultSet rs, String[] commonFields, String alias) throws SQLException {
        ProblemSet sample = getSample(rs);
        handler.map(rs, sample, commonFields, alias);
        return sample;
    }

    @Override
    public void map(ResultSet rs, ProblemSet sample) throws SQLException {
        handler.map(rs, sample);
    }

    @Override
    public ProblemSet map(ResultSet rs) throws SQLException {
        ProblemSet sample = getSample(rs);
        handler.map(rs, sample, new String[0], "");
        return sample;
    }

    private ProblemSet getSample(ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("type");
        switch (type) {
            case "contest":
                Contest c = new Contest();
                c.setSponsor(resultSet.getString("sponsor"));
                c.setVip(resultSet.getBoolean("vip"));
                return c;
            case "practice":
                return new Practice();
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
