package john.project.dao;

import java.util.List;

import john.project.models.Client;

public class ClientDAOProxy implements ClientDAO {
	private final ClientDAO target;
    private final String userRole;

    public ClientDAOProxy(ClientDAO target, String userRole) {
        this.target = target;
        this.userRole = userRole;
    }

	private void checkAdminAccess() {
        if (!"ADMIN".equalsIgnoreCase(userRole)) {
            throw new SecurityException("Access denied. Only ADMIN can perform this action.");
        }
    }
    
	@Override
	public void addClient(Client client) {
		this.checkAdminAccess();
		this.target.addClient(client);
	}

	@Override
	public void updateClient(int id, Client client) {
		this.checkAdminAccess();
		this.target.updateClient(id, client);
	}

	@Override
	public void deleteClient(int id) {
		this.checkAdminAccess();
		this.target.deleteClient(id);
	}

	@Override
	public List<Client> getAllClients() {
		return this.target.getAllClients();
	}

	@Override
	public Client getClient(int id) {
		return this.target.getClient(id);
	}

	@Override
	public Client getClient(String username, String password) {
		return this.target.getClient(username, password);
	}

	@Override
	public boolean authenticate(String username, String password) {
		return this.target.authenticate(username, password);
	}

}
