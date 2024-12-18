package john.project.order_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderResponseDto dto = new OrderResponseDto();
            dto.setId(order.getOrderId());
            dto.setOrderDate(order.getOrderDate());
            dto.setStatus(order.getStatus());
            dto.setClientId(orderRepository.findClientIdByOrderId(order.getOrderId()));
            orderResponseDtos.add(dto);
        }

        return orderResponseDtos;
    }
    
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        return toDto(order);
    }
    
    public List<Order> getOrdersByClientId(Long clientId) {
        return orderRepository.findOrdersByClientId(clientId);
    }

    public Order createOrder(Long clientId, OrderResponseDto form) {
    	Order order = new Order(form.getOrderDate(),
    			form.getStatus());
    	Order savedOrder = orderRepository.save(order);
        
        orderRepository.addOrderToClient(savedOrder.getOrderId(), clientId);
        
        return savedOrder;
    }
    
    @Transactional
    public void updateOrder(Long newClientId, OrderResponseDto form) {
        Order order = orderRepository.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + form.getId()));

        order.setOrderDate(form.getOrderDate());
        order.setStatus(form.getStatus());
        orderRepository.save(order);

        Long currentClientId = orderRepository.findClientIdByOrderId(form.getId());
        if (!currentClientId.equals(newClientId)) {
        	orderRepository.deleteByOrderId(form.getId());

        	orderRepository.addOrderToClient(form.getId(), newClientId);
        }
    }
	
	@Transactional
	public void deleteOrder(Long id, Long clientID) {
		orderRepository.deleteOrderToClient(id, clientID);
		orderRepository.deleteById(id);
	}
	
	public static Order toEntity(OrderResponseDto dto) {
        return new Order(
        		dto.getOrderDate(),
        		dto.getStatus());
    }

    public static OrderResponseDto toDto(Order order) {
    	OrderResponseDto dto = new OrderResponseDto();
    	dto.setId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        return dto;
    }
}