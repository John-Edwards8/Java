package john.project.dao;

import java.util.List;

import john.project.models.Client;

public interface ClientDAO {
	void addClient(Client client);
    void updateClient(int id, Client client);
    void deleteClient(int id);
    List<Client> getAllClients();
    public Client getClient(int id);
    
	boolean authenticate(String username, String password2);
}
