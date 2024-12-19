package john.project.client_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testFindAll() {
        Client client1 = new Client("John", "Doe", "Middle", "1234567890", "john@example.com", "password", 2L);
        Client client2 = new Client("Jane", "Smith", "Middle", "0987654321", "jane@example.com", "password", 2L);
        clientRepository.save(client1);
        clientRepository.save(client2);

        List<Client> clients = clientRepository.findAll();

        assertThat(clients).hasSize(4);
    }

    @Test
    void testFindById() {
        Client client = new Client("John", "Doe", "Middle", "1234567890", "john@example.com", "password", 2L);
        client = clientRepository.save(client);

        Optional<Client> result = clientRepository.findById(client.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John");
    }

    @Test
    void testDeleteById() {
        Client client = new Client("John", "Doe", "Middle", "1234567890", "john@example.com", "password", 2L);
        client = clientRepository.save(client);

        clientRepository.deleteById(client.getId());

        Optional<Client> result = clientRepository.findById(client.getId());
        assertThat(result).isEmpty();
    }
}
