package john.project.dao;

import java.util.List;

import john.project.models.Client;

public interface ClientDAO {
	void addClient(Client client);
    void updateClient(int id, Client client);
    void deleteClient(int id);
    List<Client> getAllClients();
    Client getClient(int id);
    Client getClient(String username, String password);
	boolean authenticate(String username, String password);
	void registerClient(Client client);
}
