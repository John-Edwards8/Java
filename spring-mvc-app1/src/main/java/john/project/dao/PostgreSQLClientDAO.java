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
	
	@Autowired
    public PostgreSQLClientDAO(Connection connection) {
        this.con = connection;
    }
	@Override
    public void addClient(Client client) {
		try {
	        String SQL = "INSERT INTO clients (client_first_name, client_second_name, client_patronymic, client_phone_number, client_email)"
	        		+ "VALUES('" + client.getName() + "', '" + client.getSurname() + "', '" + client.getPatronymic() + "', '" + client.getPhoneNumber() + "', '" + client.getEmail() + "')";
			
	        con.createStatement().executeUpdate(SQL);
	    } catch (SQLException throwables) {
	        throwables.printStackTrace();
	    }
    }

    @Override
    public void updateClient(int id, Client client) {
    	try {
	        PreparedStatement preparedStatement = con.prepareStatement("UPDATE clients SET client_first_name = ?, client_second_name = ?, client_patronymic = ?, client_phone_number = ?, client_email = ? WHERE client_id = ?");
	
	        preparedStatement.setString(1, client.getName());
	        preparedStatement.setString(2, client.getSurname());
	        preparedStatement.setString(3, client.getPatronymic());
	        preparedStatement.setString(4, client.getPhoneNumber());
	        preparedStatement.setString(5, client.getEmail());
	        preparedStatement.setInt(6, id);
	        preparedStatement.executeUpdate();
    	 } catch (SQLException throwables) {
             throwables.printStackTrace();
         }
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
        				.build();
                
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return client;
    }


    @Override
	public boolean authenticate(String username, String password) {
		String uname;
		String pass;
		
		try {
            String SQL = "SELECT client_email, client_password FROM clients;";
            ResultSet resultSet = con.createStatement().executeQuery(SQL);
            while(resultSet.next()) {
            	uname = resultSet.getString("client_email");
            	pass = resultSet.getString("client_password");
				
            	if(uname == username && pass == password) {
            		return true;
            	}
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

		return false;
	}


}