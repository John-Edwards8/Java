package john.project.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import john.project.dao.ClientDAO;
import john.project.dao.ClientDAOProxy;
import john.project.dao.ConnectionFactory;
import john.project.dao.DAOFactory;
import john.project.dao.OrderDAO;
import john.project.dao.OrderListDAO;
import john.project.dao.OrderListDAOProxy;

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
    
    @Bean(name = "realClientDAO")
    public ClientDAO realClientDAO(DAOFactory daoFactory) {
        return daoFactory.createClientDAO();
    }

    @Bean(name = "proxyClientDAO")
    public ClientDAO proxyClientDAO(@Qualifier("realClientDAO") ClientDAO realDAO) {
        return new ClientDAOProxy(realDAO, "User");
    }

    @Bean
    public OrderDAO orderDAO(DAOFactory daoFactory) {
        return daoFactory.createOrderDAO();
    }
    
    @Bean(name = "realOrderListDAO")
    public OrderListDAO realOrderListDAO(DAOFactory daoFactory) {
        return daoFactory.createOrderListDAO();
    }

    @Bean(name = "proxyOrderListDAO")
    public OrderListDAO proxyOrderListDAO(@Qualifier("realOrderListDAO") OrderListDAO realDAO) {
        return new OrderListDAOProxy(realDAO, "User");
    }
}
