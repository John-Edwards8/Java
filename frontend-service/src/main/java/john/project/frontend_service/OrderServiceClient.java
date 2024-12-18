package john.project.frontend_service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "order-service", url = "http://localhost:8082/api/orders")
public interface OrderServiceClient {
    @GetMapping
    List<OrderResponseDto> getAllOrders();

    @GetMapping("/client/{clientId}")
    List<OrderResponseDto> getOrdersByClientId(@PathVariable("clientId") Long clientId);
    
    @PostMapping("/{clientId}")
	void addOrderByClient(OrderResponseDto form, @PathVariable("clientId") Long clientId);
    
    @GetMapping("/{id}")
	OrderResponseDto getOrderById(@PathVariable("id") Long id);
    
    @PutMapping("/{id}")
   	public void updateOrder(@PathVariable("id") Long id, OrderResponseDto form);
    
    @DeleteMapping("/{orderID}/{clientID}")
	void deleteOrder(@PathVariable("orderID") Long orderID, @PathVariable("clientID") Long clientID);
}