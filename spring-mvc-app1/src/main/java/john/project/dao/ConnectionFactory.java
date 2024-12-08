package john.project.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionFactory {
	private final DataSource dataSource;
    private final String dbType;

    @Autowired
    public ConnectionFactory(DataSource dataSource, String dbType) {
        this.dataSource = dataSource;
        this.dbType = dbType;
    }
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
    }
    
    public Connection getConnection() throws SQLException {
        switch (dbType) {
        	case "mysql":
            case "postgresql":
            	return dataSource.getConnection();
            default:
                throw new SQLException("Unsupported DB type: " + dbType);
        }
    }

	public String getDbType() { return this.dbType;	}
}
