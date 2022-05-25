package ir.ac.kntu.orm.connection;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

public class ChannelFactory {
    private DataSource dataSource;

    public ChannelFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Channel createChannel() {
//        EntityManager entityManager = null;
        return new Channel(dataSource);
    }
}
