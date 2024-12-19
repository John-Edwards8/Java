package john.project.order_service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.List;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void testGetAllOrders() {
        Order order1 = new Order(1L, new Date(), "NEW");
        Order order2 = new Order(2L, new Date(), "SHIPPED");

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));
        when(orderRepository.findClientIdByOrderId(1L)).thenReturn(1L);
        when(orderRepository.findClientIdByOrderId(2L)).thenReturn(2L);

        List<OrderResponseDto> orders = orderService.getAllOrders();

        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getId()).isEqualTo(1L);
        assertThat(orders.get(0).getClientId()).isEqualTo(1L);
        assertThat(orders.get(1).getStatus()).isEqualTo("SHIPPED");
    }

    @Test
    void testGetOrderById() {
        Order order = new Order(1L, new Date(), "NEW");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDto orderDto = orderService.getOrderById(1L);

        assertThat(orderDto).isNotNull();
        assertThat(orderDto.getId()).isEqualTo(1L);
        assertThat(orderDto.getStatus()).isEqualTo("NEW");
    }

    @Test
    void testGetOrdersByClientId() {
        Order order1 = new Order(1L, new Date(), "NEW");
        Order order2 = new Order(2L, new Date(), "SHIPPED");

        when(orderRepository.findOrdersByClientId(1L)).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getOrdersByClientId(1L);

        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getOrderId()).isEqualTo(1L);
    }

    @Test
    void testCreateOrder() {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderDate(new Date());
        dto.setStatus("NEW");

        Order order = new Order(1L, dto.getOrderDate(), dto.getStatus());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(1L, dto);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getOrderId()).isEqualTo(1L);

        verify(orderRepository).addOrderToClient(1L, 1L);
    }

    @Test
    void testUpdateOrder() {
        Order order = new Order(1L, new Date(), "NEW");
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(1L);
        dto.setOrderDate(Date.from(LocalDate.now().plusDays(1)
								                .atStartOfDay()
								                .atZone(ZoneId.systemDefault())
								                .toInstant()));
        dto.setStatus("SHIPPED");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.findClientIdByOrderId(1L)).thenReturn(1L);

        orderService.updateOrder(2L, dto);

        verify(orderRepository).save(order);
        verify(orderRepository).deleteByOrderId(1L);
        verify(orderRepository).addOrderToClient(1L, 2L);
    }

    @Test
    void testDeleteOrder() {
        orderService.deleteOrder(1L, 1L);

        verify(orderRepository).deleteOrderToClient(1L, 1L);
        verify(orderRepository).deleteById(1L);
    }
}
