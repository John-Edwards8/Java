package john.project.dao;

import java.util.List;

import john.project.models.Order;
import john.project.models.OrderList;

public class OrderListDAOProxy implements OrderListDAO {
	private final OrderListDAO target;
    private final String userRole;

    public OrderListDAOProxy(OrderListDAO target, String userRole) {
        this.target = target;
        this.userRole = userRole;
    }

	private void checkAdminAccess() {
        if (!"ADMIN".equalsIgnoreCase(userRole)) {
            throw new SecurityException("Access denied. Only ADMIN can perform this action.");
        }
    }
	@Override
	public void addOrderList(Order order, int clientId) {
		this.target.addOrderList(order, clientId);
	}

	@Override
	public void updateOrderList(Order order, int clientID) {
		this.checkAdminAccess();
		this.target.updateOrderList(order, clientID);
	}

	@Override
	public void deleteOrderList(int orderID, int clientID) {
		this.checkAdminAccess();
		this.target.deleteOrderList(orderID, clientID);
	}

	@Override
	public List<OrderList> getOrderList(int id) {
		return this.target.getOrderList(id);
	}

	@Override
	public List<OrderList> getAllOrderLists() {
		return this.target.getAllOrderLists();
	}

}
