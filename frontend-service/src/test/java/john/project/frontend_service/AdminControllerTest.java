package john.project.frontend_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = null;
    Date date2 = null;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientServiceClient clientServiceClient;

    @MockBean
    private OrderServiceClient orderServiceClient;

    @MockBean
    private OrderHistory orderHistory;

    @Test
    void testHelloPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/hello"));
    }

    @Test
    void testOrdersPage() throws Exception {
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
                new OrderResponseDto(1L, date1, "NEW", 10L),
                new OrderResponseDto(2L, date2, "COMPLETED", 7L)
        );
        List<ClientResponseDto> clients = Arrays.asList(
                new ClientResponseDto(7L, "John", "Doe", null, "987654321", "john.doe@example.com", "123456789", 2L),
                new ClientResponseDto(8L, "Jane", "Smith", null, "123456789", "jane.smith@example.com", "987654321", 2L)
        );
        when(orderServiceClient.getAllOrders()).thenReturn(orders);
        when(clientServiceClient.getClientById(anyLong())).thenReturn(clients.get(0), clients.get(1));

        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("admin/orders"));

        verify(orderServiceClient).getAllOrders();
        verify(clientServiceClient, times(2)).getClientById(anyLong());
    }

    @Test
    void testNewOrderForm() throws Exception {
    	try {
    		date1 = formatter.parse("2024-12-19");
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	OrderResponseDto previousOrder = new OrderResponseDto(null, date1, "PENDING", null);
        when(orderHistory.restoreState()).thenReturn(previousOrder);

        // Act & Assert
        mockMvc.perform(get("/admin/new-order"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(view().name("admin/new"));
    }

    @Test
    void testAddOrderList() throws Exception {
    	try {
    		date2 = formatter.parse("2024-12-20");
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
        OrderResponseDto order = new OrderResponseDto(null, date2, "PENDING", null);
        when(orderHistory.restoreState()).thenReturn(order);

        mockMvc.perform(post("/admin/new-orderlist")
                .param("clientId", "10")
                .param("action", "confirm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));

        verify(orderServiceClient).addOrderByClient(order, 10L);
    }

    @Test
    void testAddClient() throws Exception {
        ClientResponseDto client = new ClientResponseDto(null, "Alice", "Brown", null, "555555555", "alice.brown@example.com", "555555555", 2L);

        mockMvc.perform(post("/admin/client")
                .flashAttr("client", client))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/clients?message=Client+successfully+added%3A+Alice"));

        verify(clientServiceClient).createClient(any(ClientResponseDto.class));
    }

    @Test
    void testUpdateClient() throws Exception {
        ClientResponseDto updatedClient = new ClientResponseDto(null, "Alice", "Brown", null, "555555555", "alice.brown@example.com", "555555555", 2L);

        mockMvc.perform(post("/admin/update-client/11")
                .flashAttr("client", updatedClient))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/clients?message=Client+successfully+updated%3A+Alice"));

        verify(clientServiceClient).updateClient(eq(11L), any(ClientResponseDto.class));
    }

    @Test
    void testDeleteOrder() throws Exception {
        mockMvc.perform(post("/admin/delete-order/1/10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));

        verify(orderServiceClient).deleteOrder(1L, 10L);
    }
       
}
