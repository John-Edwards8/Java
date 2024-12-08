package john.project.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import john.project.dao.ClientDAO;
import john.project.dao.ConnectionFactory;
import john.project.dao.DAOFactory;
import john.project.dao.OrderDAO;
import john.project.dao.OrderListDAO;

@Configuration
public class DAOConfig {
	@Value("${db.type:postgresql}")
    private String dbType;

    @Bean
    public ConnectionFactory connectionFactory(DataSource dataSource) {
        return new ConnectionFactory(dataSource, dbType);
    }

    @Bean
    public DAOFactory daoFactory(ConnectionFactory connectionFactory) {
        return new DAOFactory(connectionFactory);
    }

    @Bean
    public ClientDAO clientDAO(DAOFactory daoFactory) {
        return daoFactory.createClientDAO();
    }

    @Bean
    public OrderDAO orderDAO(DAOFactory daoFactory) {
        return daoFactory.createOrderDAO();
    }

    @Bean
    public OrderListDAO orderListDAO(DAOFactory daoFactory) {
        return daoFactory.createOrderListDAO();
    }
}
