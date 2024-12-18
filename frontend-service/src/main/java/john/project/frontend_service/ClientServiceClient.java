package john.project.frontend_service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@FeignClient(name = "client-service", url = "http://localhost:8081/api/clients")
@Component
public interface ClientServiceClient {
    @GetMapping
    public List<ClientResponseDto> getAllClients();
    
	@GetMapping("/{id}")
    ClientResponseDto getClientById(@PathVariable("id") Long id);

    @PostMapping
    public ClientResponseDto createClient(@Valid ClientResponseDto client);
    
    @PutMapping("/{id}")
	public void updateClient(@PathVariable("id")Long id, @Valid ClientResponseDto form);
    
    @DeleteMapping("/{id}")
	void deleteClient(@PathVariable("id") Long id);
}