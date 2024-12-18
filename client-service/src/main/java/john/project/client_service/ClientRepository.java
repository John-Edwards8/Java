package john.project.client_service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

	List<Client> findAll();

	Optional<Client> findById(Long id);

	void deleteById(Long id);

}
