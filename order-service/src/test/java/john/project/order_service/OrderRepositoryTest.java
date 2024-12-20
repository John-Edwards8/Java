package john.project.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testFindOrdersByClientId() {
        Order order1 = new Order(null, new Date(), "NEW");
        Order order2 = new Order(null, new Date(), "SHIPPED");
        order1 = orderRepository.save(order1);
        order2 = orderRepository.save(order2);
        orderRepository.addOrderToClient(order1.getOrderId(), 10L);
        orderRepository.addOrderToClient(order2.getOrderId(), 10L);

        List<Order> orders = orderRepository.findOrdersByClientId(10L);

        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getOrderId()).isEqualTo(order1.getOrderId());
        assertThat(orders.get(1).getStatus()).isEqualTo("SHIPPED");
    }

    @Test
    void testAddOrderToClient() {
        Order order = new Order(null, new Date(), "NEW");
        order = orderRepository.save(order);

        orderRepository.addOrderToClient(order.getOrderId(), 10L);

        List<Order> orders = orderRepository.findOrdersByClientId(10L);
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderId()).isEqualTo(order.getOrderId());
    }

    @Test
    @Transactional
    void testDeleteOrderToClient() {
        Order order = new Order(null, new Date(), "NEW");
        order = orderRepository.save(order);
        orderRepository.addOrderToClient(order.getOrderId(), 10L);

        orderRepository.deleteOrderToClient(order.getOrderId(), 10L);

        List<Order> orders = orderRepository.findOrdersByClientId(10L);
        assertThat(orders).isEmpty();
    }

    @Test
    void testFindClientIdByOrderId() {
        Order order = new Order(null, new Date(), "NEW");
        order = orderRepository.save(order);
        orderRepository.addOrderToClient(order.getOrderId(), 10L);

        Long clientId = orderRepository.findClientIdByOrderId(order.getOrderId());

        assertThat(clientId).isEqualTo(10L);
    }

    @Test
    @Transactional
    void testDeleteByOrderId() {
        Order order = new Order(null, new Date(), "NEW");
        order = orderRepository.save(order);
        orderRepository.addOrderToClient(order.getOrderId(), 10L);

        orderRepository.deleteByOrderId(order.getOrderId());

        Long clientId = orderRepository.findClientIdByOrderId(order.getOrderId());
        assertThat(clientId).isNull();
    }
}
