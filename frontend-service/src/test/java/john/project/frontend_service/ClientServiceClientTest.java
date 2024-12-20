package john.project.frontend_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClientServiceClientTest {

    @MockBean
    private ClientServiceClient clientServiceClient;

    @Test
    void testGetAllClients() {
        List<ClientResponseDto> mockClients = List.of(new ClientResponseDto(1L, "John", "Doe", null, "123456789", "john@example.com", null, 2L));
        when(clientServiceClient.getAllClients()).thenReturn(mockClients);

        List<ClientResponseDto> clients = clientServiceClient.getAllClients();

        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(1);
    }

    @Test
    void testGetClientById() {
        Long clientId = 1L;
        ClientResponseDto mockClient = new ClientResponseDto(clientId, "John", "Doe", null, "123456789", "john@example.com", null, 2L);
        when(clientServiceClient.getClientById(clientId)).thenReturn(mockClient);

        ClientResponseDto client = clientServiceClient.getClientById(clientId);

        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(clientId);
    }
    
    @Test
    void testCreateClient() {
        ClientResponseDto request = new ClientResponseDto(null, "John", "Doe", null, "123456789", "john@example.com", "password", 2L);
        ClientResponseDto response = new ClientResponseDto(7L, "John", "Doe", null, "123456789", "john@example.com", "password", 2L);
        when(clientServiceClient.createClient(request)).thenReturn(response);

        ClientResponseDto createdClient = clientServiceClient.createClient(request);

        assertThat(createdClient).isNotNull();
        assertThat(createdClient.getId()).isEqualTo(7L);
        assertThat(createdClient.getName()).isEqualTo("John");
        verify(clientServiceClient).createClient(request);
    }

    @Test
    void testUpdateClient() {
        Long clientId = 10L;
        ClientResponseDto updateForm = new ClientResponseDto(clientId, "Jane", "Doe", null, "987654321", "jane@example.com", "newpassword", 2L);
        doNothing().when(clientServiceClient).updateClient(clientId, updateForm);

        // Act
        clientServiceClient.updateClient(clientId, updateForm);

        verify(clientServiceClient).updateClient(clientId, updateForm);
    }

    @Test
    void testDeleteClient() {
        Long clientId = 10L;
        doNothing().when(clientServiceClient).deleteClient(clientId);

        clientServiceClient.deleteClient(clientId);

        verify(clientServiceClient).deleteClient(clientId);
    }
}
