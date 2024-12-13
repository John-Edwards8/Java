package john.project.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import john.project.dao.ClientDAO;
import john.project.dao.GUIObserver;

@Configuration
public class AppConfig {
    
    private static Connection singleConnection;
    
    @Bean
    public Connection connection() throws SQLException {
        if (singleConnection == null || singleConnection.isClosed()) {
            DataSource dataSource = dataSource();
            singleConnection = dataSource.getConnection();
        }
        return singleConnection;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/userdb");
        dataSource.setUsername("postgres");
        dataSource.setPassword("1111");
        return dataSource;
    }
    
    @Autowired
    public void configureDAO(@Qualifier("realClientDAO") ClientDAO clientDAO, GUIObserver guiObserver) {
        clientDAO.addObserver(guiObserver);
    }
}
