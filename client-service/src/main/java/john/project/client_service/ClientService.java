package john.project.client_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    public Client createClient(ClientCreateDto form) {
    	Client client = new Client(form.getName(),
    			form.getSurname(),
    			form.getPatronymic(),
    			form.getPhoneNumber(),
    			form.getEmail(),
    			form.getPass(),
    			(long)2);
        return clientRepository.save(client);
    }
    
    @Transactional
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
    
    @Transactional
    public void updateClient(Long id, ClientResponseDto form) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setName(form.getName());
        client.setSurname(form.getSurname());
        client.setPatronymic(form.getPatronymic());
        client.setEmail(form.getEmail());
        client.setPhoneNumber(form.getPhoneNumber());
        client.setPass(form.getPass());

        clientRepository.save(client);
    }
}