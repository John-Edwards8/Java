package john.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import john.project.models.Order;
import john.project.models.OrderList;

@Component
public class PostgreSQLOrderListDAO implements OrderListDAO {
	private final Connection con;

	@Autowired
    public PostgreSQLOrderListDAO(Connection connection) {
        this.con = connection;
    }

    @Override
	public void addOrderList(Order order, int clientId) {
    	new PostgreSQLOrderDAO(con).addOrderByAdmin(order);
		
		try {
            String SQL = "INSERT INTO orderlist (order_id, client_id) VALUES ((SELECT currval(pg_get_serial_sequence('orders', 'order_id')))," + clientId + ");";
            con.createStatement().executeUpdate(SQL);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	}

    @Override
    public void updateOrderList(Order order, int clientID) {
    	new PostgreSQLOrderDAO(con).updateOrder(order.getId(), order);

        try {
        	PreparedStatement preparedStatement = con.prepareStatement("UPDATE orderlist SET order_id=?, client_id=? WHERE order_id=?;");
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setInt(2, clientID);
            preparedStatement.setInt(3, order.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteOrderList(int orderID, int clientID) {
    	try {
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM orderlist WHERE order_id=? AND client_id=?;");
            preparedStatement.setInt(1, orderID);
            preparedStatement.setInt(2, clientID);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<OrderList> getOrderList(int id) {
    	List<OrderList> orderLists = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM clients c INNER JOIN orderlist ol ON c.client_id = ol.client_id\r\n"
            		+ "INNER JOIN orders o ON ol.order_id = o.order_id\r\n"
            		+ "WHERE c.client_id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {        
            	OrderList list = new OrderList.OrderListBuilder()
	            		.client(id,
	            				resultSet.getString("client_first_name"),
	            				resultSet.getString("client_second_name"),
	            				resultSet.getString("client_patronymic"),
	            				resultSet.getString("client_phone_number"),
	            				resultSet.getString("client_email"))
	            		.order(resultSet.getInt("order_id"),
	            			   resultSet.getDate("order_date"),
	            			   resultSet.getString("order_status"))
	            		.build();
            	orderLists.add(list);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orderLists;
    }

    @Override
    public List<OrderList> getAllOrderLists() {
    	List<OrderList> orderLists = new ArrayList<>();

        try {
            String SQL = "SELECT * FROM clients c INNER JOIN orderlist ol ON c.client_id = ol.client_id INNER JOIN orders o ON o.order_id = ol.order_id;";
            ResultSet resultSet = con.createStatement().executeQuery(SQL);
            while(resultSet.next()) {         	
            	OrderList list = new OrderList.OrderListBuilder()
                		.client(resultSet.getInt("client_id"),
                				resultSet.getString("client_first_name"),
                				resultSet.getString("client_second_name"),
                				resultSet.getString("client_patronymic"),
                				resultSet.getString("client_phone_number"),
                				resultSet.getString("client_email"))
                		.order(resultSet.getInt("order_id"),
                			   resultSet.getDate("order_date"),
                			   resultSet.getString("order_status"))
                		.build();
            					
            	orderLists.add(list);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orderLists;
    }
}
