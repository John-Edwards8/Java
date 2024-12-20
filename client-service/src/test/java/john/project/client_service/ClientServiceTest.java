package john.project.client_service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    public ClientServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClients() {
        Client client1 = new Client("John", "Doe", "Middle", "1234567890", "john@example.com", "password", 2L);
        Client client2 = new Client("Jane", "Smith", "Middle", "0987654321", "jane@example.com", "password", 2L);
        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<Client> clients = clientService.getAllClients();

        assertThat(clients).hasSize(2);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testGetClientById() {
        Client client = new Client("John", "Doe", "Middle", "1234567890", "john@example.com", "password", 2L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client result = clientService.getClientById(1L);

        assertThat(result).isEqualTo(client);
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetClientByIdNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.getClientById(1L));
        assertThat(exception.getMessage()).isEqualTo("Client not found with id: 1");
    }

    @Test
    void testCreateClient() {
        ClientCreateDto form = new ClientCreateDto("John", "Doe", "Middle", "1234567890", "john@example.com", "password", 2L);
        Client client = new Client(form.getName(), form.getSurname(), form.getPatronymic(), form.getPhoneNumber(), form.getEmail(), form.getPass(), 2L);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.createClient(form);

        assertThat(result).isEqualTo(client);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testDeleteClient() {
        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateClient() {
        Client client = new Client("John", "Doe", "Middle", "1234567890", "john@example.com", "password", 2L);
        ClientResponseDto form = new ClientResponseDto("Jane", "Smith", "Middle", "0987654321", "jane@example.com", "newpassword", 2L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.updateClient(1L, form);

        assertThat(client.getName()).isEqualTo("Jane");
        assertThat(client.getEmail()).isEqualTo("jane@example.com");
        verify(clientRepository, times(1)).save(client);
    }
}
