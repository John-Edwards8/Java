package john.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import john.project.models.Order;

@Component
public class PostgreSQLOrderDAO implements OrderDAO {
	private final Connection con;
	
	@Autowired
    public PostgreSQLOrderDAO(Connection connection) {
        this.con = connection;
    }

    @Override
    public void addOrderByAdmin(Order order) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	try {
	        String SQL = "INSERT INTO orders (order_date, order_status) VALUES('" + formatter.format(order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + "', '" + order.getStatus() + "')";
		
	        con.createStatement().executeUpdate(SQL);
	    } catch (SQLException throwables) {
	        throwables.printStackTrace();
	    }
    }
    
    @Override
    public void updateOrder(int id, Order order) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	 try {
	        PreparedStatement preparedStatement = con.prepareStatement("UPDATE orders SET order_status = ?, order_date='" + formatter.format(order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + "' WHERE order_id = ?");
	
	        preparedStatement.setString(1, order.getStatus());
	        preparedStatement.setInt(2, id);
	        preparedStatement.executeUpdate();
    	 } catch (SQLException throwables) {
             throwables.printStackTrace();
         }
    }

    @Override
    public void deleteOrder(int id) {
    	try {
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM orders WHERE order_id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
    @Override
    public Order getOrder(int id) {
    	Order order = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM orders WHERE order_id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
	            order = new Order
	            		.OrderBuilder()
	            		.id(id)
	            		.date(resultSet.getDate("order_date"))
	            		.status(resultSet.getString("order_status"))
	            		.build();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return order;
    }
    
    @Override
    public List<Order> getAllOrders() {
    	List<Order> orders = new ArrayList<>();

        try {
            String SQL = "SELECT * FROM orders";
            ResultSet resultSet = con.createStatement().executeQuery(SQL);

            while(resultSet.next()) {       	
            	Order order = new Order
	            		.OrderBuilder()
	            		.id(resultSet.getInt("order_id"))
	            		.date(resultSet.getDate("order_date"))
	            		.status(resultSet.getString("order_status"))
	            		.build();

                orders.add(order);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orders;
    }

}
