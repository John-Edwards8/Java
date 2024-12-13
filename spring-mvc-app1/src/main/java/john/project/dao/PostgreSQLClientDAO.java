package john.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import john.project.models.Client;

@Component
public class PostgreSQLClientDAO implements ClientDAO {
	private final Connection con;
	private List<Observer> observers = new ArrayList<>();

	@Autowired
    public PostgreSQLClientDAO(Connection connection) {
        this.con = connection;
    }
	
	@Override
    public void addObserver(Observer obs) {
    	observers.add(obs);
    }
	
	@Override
    public void removeObserver(Observer obs) {
    	observers.remove(obs);
    }
	
	@Override
	public void notifyObservers(String eventType, Object entity) {
        for (Observer obs : observers) {
        	obs.update(eventType, entity);
        }
    }
	
	@Override
    public void addClient(Client client) {
		try {
	        String SQL = "INSERT INTO clients (client_first_name, client_second_name, client_patronymic, client_phone_number, client_email, client_password, client_role_id)"
	        		+ "VALUES('" + client.getName() + "', '" + client.getSurname() + "', '" + client.getPatronymic() + "', '" + client.getPhoneNumber() + "', '" + client.getEmail() + "', '" + client.getPass() + "', 2)";
			
	        con.createStatement().executeUpdate(SQL);
	    } catch (SQLException throwables) {
	        throwables.printStackTrace();
	    }
		notifyObservers("CLIENT_ADDED", client);
    }	
	
    @Override
    public void updateClient(int id, Client client) {
    	try {
	        PreparedStatement preparedStatement = con.prepareStatement("UPDATE clients SET client_first_name = ?, client_second_name = ?, client_patronymic = ?, client_phone_number = ?, client_email = ?, client_password = ? WHERE client_id = ?");
	
	        preparedStatement.setString(1, client.getName());
	        preparedStatement.setString(2, client.getSurname());
	        preparedStatement.setString(3, client.getPatronymic());
	        preparedStatement.setString(4, client.getPhoneNumber());
	        preparedStatement.setString(5, client.getEmail());
	        preparedStatement.setString(6, client.getPass());
	        preparedStatement.setInt(7, id);
	        preparedStatement.executeUpdate();
    	 } catch (SQLException throwables) {
             throwables.printStackTrace();
         }
    	notifyObservers("CLIENT_UPDATED", client);
    }

    @Override
    public void deleteClient(int id) {
    	try {
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM clients WHERE client_id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    	notifyObservers("CLIENT_DELETED", id);
    }

    @Override
    public List<Client> getAllClients() {
    	List<Client> clients = new ArrayList<>();

        try {
            Statement statement = con.createStatement();
            String SQL = "SELECT * FROM clients";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next()) {          	
                Client client = new Client
        				.ClientBuilder()
        				.id(resultSet.getInt("client_id"))
        				.name(resultSet.getString("client_first_name"))
        				.surname(resultSet.getString("client_second_name"))
        				.patronymic(resultSet.getString("client_patronymic"))
        				.phoneNumber(resultSet.getString("client_phone_number"))
        				.email(resultSet.getString("client_email"))
        				.pass(resultSet.getString("client_password"))
        				.build();

                clients.add(client);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return clients;
    }
    
    @Override
    public Client getClient(int id) {
    	Client client = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM clients WHERE client_id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {               
                client = new Client
        				.ClientBuilder()
        				.id(id)
        				.name(resultSet.getString("client_first_name"))
        				.surname(resultSet.getString("client_second_name"))
        				.patronymic(resultSet.getString("client_patronymic"))
        				.phoneNumber(resultSet.getString("client_phone_number"))
        				.email(resultSet.getString("client_email"))
        				.pass(resultSet.getString("client_password"))
        				.build();
                
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return client;
    }
        
    @Override
    public Client getClient(String username, String password) {
        String SQL = "SELECT * FROM clients WHERE client_email = ? AND client_password = ?";
        
        try (PreparedStatement stmt = con.prepareStatement(SQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
            	Client client = new Client
        				.ClientBuilder()
        				.id(resultSet.getInt("client_id"))
        				.name(resultSet.getString("client_first_name"))
        				.surname(resultSet.getString("client_second_name"))
        				.patronymic(resultSet.getString("client_patronymic"))
        				.phoneNumber(resultSet.getString("client_phone_number"))
        				.email(resultSet.getString("client_email"))
        				.pass(resultSet.getString("client_password"))
        				.build();
                return client;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
    
    @Override
    public boolean authenticate(String username, String password) {
        String SQL = "SELECT client_email FROM clients WHERE client_email = ? AND client_password = ?";
        
        try (PreparedStatement stmt = con.prepareStatement(SQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }
	@Override
	public void registerClient(Client client) {
		this.addClient(client);
	}

}
