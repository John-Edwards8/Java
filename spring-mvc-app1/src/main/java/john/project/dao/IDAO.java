package john.project.dao;
import java.util.List;
import john.project.models.Client;
import john.project.models.Order;
import john.project.models.OrderList;

public interface IDAO {
	void addClient(Client client);
    void updateClient(int id, Client client);
    void deleteClient(int id);
    List<Client> getAllClients();
	Client getClientByFullName(String fullName);
	
	void addOrderByUser(Order order);
	void addOrderByAdmin(Order order);
    void updateOrder(int id, Order order);
    void deleteOrder(int id);
    List<Order> getAllOrders();
    
    void addOrderList(OrderList orderList);
    void updateOrderList(OrderList orderList);
    void deleteOrderList(Client client, Order order);
    OrderList getOrderList(Client client, Order order);
    List<OrderList> getAllOrderLists();
    
	boolean authenticate(String username, String password2);
}
