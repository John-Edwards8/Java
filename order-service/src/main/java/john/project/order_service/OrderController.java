package john.project.order_service;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    @GetMapping("/client/{clientId}")
    public List<Order> getOrdersByClientId(@PathVariable Long clientId) {
        return orderService.getOrdersByClientId(clientId);
    }
    
    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable("id") Long id) {
		return orderService.getOrderById(id);
    }
    
    @PostMapping("/{clientId}")
    public Order createOrder(@RequestBody OrderResponseDto order,  @PathVariable("clientId") Long clientId) {
        return orderService.createOrder(clientId, order);
    }
    
    @PutMapping("/{id}")
   	public void updateOrder(@PathVariable Long id, @RequestBody OrderResponseDto form) {
    	orderService.updateOrder(id, form);
    }

    @DeleteMapping("/{orderID}/{clientID}")
    public void deleteOrder(@PathVariable Long orderID, @PathVariable Long clientID) {
    	orderService.deleteOrder(orderID, clientID);
    }
}