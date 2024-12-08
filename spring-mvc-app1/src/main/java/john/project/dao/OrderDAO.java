package john.project.dao;

import java.util.List;

import john.project.models.Order;

public interface OrderDAO {
	void addOrderByAdmin(Order order);
    void updateOrder(int id, Order order);
    void deleteOrder(int id);
    List<Order> getAllOrders();
    public Order getOrder(int id);

}
