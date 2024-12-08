package john.project.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DAOFactory {
    private final ConnectionFactory connectionFactory;
    
    @Autowired
    public DAOFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ClientDAO createClientDAO() {
        try {
            Connection connection = connectionFactory.getConnection();
            return new PostgreSQLClientDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating ClientDAO", e);
        }
    }

    public OrderDAO createOrderDAO() {
    	try {
            Connection connection = connectionFactory.getConnection();
            return new PostgreSQLOrderDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating OrderDAO", e);
        }
    }

    public OrderListDAO createOrderListDAO() {
    	try {
            Connection connection = connectionFactory.getConnection();
            return new PostgreSQLOrderListDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating OrderListDAO", e);
        }
    }
}
