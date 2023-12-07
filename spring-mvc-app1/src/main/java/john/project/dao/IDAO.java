package john.project.dao;
import java.util.*;

import john.project.models.Client;
import john.project.models.Order;
import john.project.models.OrderList;

public interface IDAO {
	HashMap<String, Order> getCache();
	HashMap<String, Client> getCli();
	
	void addClient(Client client);
    void updateClient(int id, Client client);
    void deleteClient(int id);
    List<Client> getAllClients();
	
	void addOrderByAdmin(Order order);
    void updateOrder(int id, Order order);
    void deleteOrder(int id);
    List<Order> getAllOrders();
    List<Order> getAllOrdersByClient(int clientID);
    
    void addOrderList(Order order, int clientId);
    void updateOrderList(Order order, int clientID);
    void deleteOrderList(int orderID, int clientID);
    OrderList getOrderList(int id);
    List<OrderList> getAllOrderLists();
    
	boolean authenticate(String username, String password2);
}
