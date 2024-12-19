package john.project.frontend_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceClientTest {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = null;
    Date date2 = null;
	

    @MockBean
    private OrderServiceClient orderServiceClient;

    @Test
    void testGetAllOrders() {
    	try {
    		date1 = formatter.parse("2024-12-19");
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	try {
    		date2 = formatter.parse("2024-12-20");
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	List<OrderResponseDto> orders = Arrays.asList(
                new OrderResponseDto(1L, date1, "NEW"),
                new OrderResponseDto(2L, date2, "COMPLETED")
        );
        when(orderServiceClient.getAllOrders()).thenReturn(orders);

        // Act
        List<OrderResponseDto> result = orderServiceClient.getAllOrders();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        verify(orderServiceClient).getAllOrders();
    }

    @Test
    void testGetOrdersByClientId() {
        Long clientId = 10L;
        try {
    		date1 = formatter.parse("2024-12-19");
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
        try {
    		date2 = formatter.parse("2024-12-20");
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
        
        List<OrderResponseDto> orders = Arrays.asList(
                new OrderResponseDto(1L, date1, "NEW"),
                new OrderResponseDto(2L, date2, "COMPLETED")
        );
        when(orderServiceClient.getOrdersByClientId(clientId)).thenReturn(orders);

        List<OrderResponseDto> result = orderServiceClient.getOrdersByClientId(clientId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        verify(orderServiceClient).getOrdersByClientId(clientId);
    }

    @Test
    void testAddOrderByClient() {
        Long clientId = 10L;
        try {
    		date1 = formatter.parse("2024-12-19");
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
        OrderResponseDto order = new OrderResponseDto(null, date1, "NEW");
        doNothing().when(orderServiceClient).addOrderByClient(order, clientId);

        orderServiceClient.addOrderByClient(order, clientId);

        verify(orderServiceClient).addOrderByClient(order, clientId);
    }

    @Test
    void testGetOrderById() {
        Long orderId = 1L;
		try {
			date1 = formatter.parse("2024-12-19");
		} catch (ParseException e) {
			e.printStackTrace();
		}
        OrderResponseDto order = new OrderResponseDto(orderId, date1, "NEW");
        when(orderServiceClient.getOrderById(orderId)).thenReturn(order);

        OrderResponseDto result = orderServiceClient.getOrderById(orderId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
        verify(orderServiceClient).getOrderById(orderId);
    }

    @Test
    void testUpdateOrder() {
        Long orderId = 1L;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
    		date2 = formatter.parse("2024-12-20");
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
        OrderResponseDto order = new OrderResponseDto(orderId, date2, "COMPLETED");
        doNothing().when(orderServiceClient).updateOrder(orderId, order);

        orderServiceClient.updateOrder(orderId, order);

        verify(orderServiceClient).updateOrder(orderId, order);
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;
        Long clientId = 10L;
        doNothing().when(orderServiceClient).deleteOrder(orderId, clientId);

        orderServiceClient.deleteOrder(orderId, clientId);

        verify(orderServiceClient).deleteOrder(orderId, clientId);
    }
}
