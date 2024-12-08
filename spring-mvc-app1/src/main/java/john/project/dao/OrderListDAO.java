package john.project.dao;

import java.util.List;

import john.project.models.Order;
import john.project.models.OrderList;

public interface OrderListDAO {
    void addOrderList(Order order, int clientId);
    void updateOrderList(Order order, int clientID);
    void deleteOrderList(int orderID, int clientID);
    OrderList getOrderList(int id);
    List<OrderList> getAllOrderLists();

}
