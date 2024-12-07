package john.project.dao;

import java.sql.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import john.project.models.Client;
import john.project.models.ClientForm;
import john.project.models.Order;
import john.project.models.OrderForm;
import john.project.models.OrderList;

@Component
public class PostgreSQLDAO implements IDAO {
	private static final String URL = "jdbc:postgresql://localhost:5432/userdb";
	private static final String USER = "postgres";
	private static final String PASSWORD = "1111";

	private static Connection con;
    private HashMap<String, OrderForm> cacheOrder;
    private HashMap<String, ClientForm> cacheCli;

    @Autowired
    public PostgreSQLDAO() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        this.cacheOrder = new HashMap<>();
        this.cacheCli = new HashMap<>();

    }
	
	@Override
    public void addClient(Client client) {
		try {
	        String SQL = "INSERT INTO clients (client_first_name, client_second_name, client_patronymic, client_phone_number, client_email)"
	        		+ "VALUES('" + client.getName() + "', '" + client.getSurname() + "', '" + client.getPatronymic() + "', '" + client.getPhoneNumber() + "', '" + client.getEmail() + "')";
			
	        con.createStatement().executeUpdate(SQL);
	    } catch (SQLException throwables) {
	        throwables.printStackTrace();
	    }
    }

    @Override
    public void updateClient(int id, Client client) {
    	try {
	        PreparedStatement preparedStatement = con.prepareStatement("UPDATE clients SET client_first_name = ?, client_second_name = ?, client_patronymic = ?, client_phone_number = ?, client_email = ? WHERE client_id = ?");
	
	        preparedStatement.setString(1, client.getName());
	        preparedStatement.setString(2, client.getSurname());
	        preparedStatement.setString(3, client.getPatronymic());
	        preparedStatement.setString(4, client.getPhoneNumber());
	        preparedStatement.setString(5, client.getEmail());
	        preparedStatement.setInt(6, id);
	        preparedStatement.executeUpdate();
    	 } catch (SQLException throwables) {
             throwables.printStackTrace();
         }
    }

    @Override
    public void deleteClient(int id) {
    	try {
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM clients WHERE client_id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Client> getAllClients() {
    	List<Client> clients = new ArrayList<>();

        try {
            Statement statement = con.createStatement();
            String SQL = "SELECT * FROM clients";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next()) {          	
                Client client = new Client
        				.ClientBuilder()
        				.id(resultSet.getInt("client_id"))
        				.name(resultSet.getString("client_first_name"))
        				.surname(resultSet.getString("client_second_name"))
        				.patronymic(resultSet.getString("client_patronymic"))
        				.phoneNumber(resultSet.getString("client_phone_number"))
        				.email(resultSet.getString("client_email"))
        				.build();

                clients.add(client);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return clients;
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
    
    public Client getClient(int id) {
    	Client client = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM clients WHERE client_id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {               
                client = new Client
        				.ClientBuilder()
        				.id(id)
        				.name(resultSet.getString("client_first_name"))
        				.surname(resultSet.getString("client_second_name"))
        				.patronymic(resultSet.getString("client_patronymic"))
        				.phoneNumber(resultSet.getString("client_phone_number"))
        				.email(resultSet.getString("client_email"))
        				.build();
                
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return client;
    }
    
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


    @Override
	public void addOrderList(Order order, int clientId) {
		this.addOrderByAdmin(order);
		
		try {
            String SQL = "INSERT INTO orderlist (order_id, client_id) VALUES ((SELECT currval(pg_get_serial_sequence('orders', 'order_id')))," + clientId + ");";
            con.createStatement().executeUpdate(SQL);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	}

    @Override
    public void updateOrderList(Order order, int clientID) {
        this.updateOrder(order.getId(), order);

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
    public OrderList getOrderList(int id) {
    	OrderList list = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM clients c INNER JOIN orderlist ol ON c.client_id = ol.client_id\r\n"
            		+ "INNER JOIN orders o ON ol.order_id = o.order_id\r\n"
            		+ "WHERE o.order_id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
                        
            list = new OrderList.OrderListBuilder()
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
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
    
    @Override
	public boolean authenticate(String username, String password) {
		String uname;
		String pass;
		
		try {
            String SQL = "SELECT client_email, client_password FROM clients;";
            ResultSet resultSet = con.createStatement().executeQuery(SQL);
            while(resultSet.next()) {
            	uname = resultSet.getString("client_email");
            	pass = resultSet.getString("client_password");
				
            	if(uname == username && pass == password) {
            		return true;
            	}
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

		return false;
	}

	@Override
	public HashMap<String, OrderForm> getCache() {
		return this.cacheOrder;
	}
	
	@Override
	public HashMap<String, ClientForm> getCli() {
		return this.cacheCli;
	}

	public void save(String key, OrderForm order) {
		cacheOrder.put(key, order);
	}
	
	public OrderForm get(String key) {
		return this.cacheOrder.get(key);
	}

	public void deleteOrder(String string) {
		this.cacheOrder.remove(string);
	}

	public void save(String key, ClientForm client) {
		cacheCli.put(key, client);
		
	}
	
	public ClientForm getLast(String key) {
		return this.cacheCli.get(key);
	}
	
	public void deleteClient(String string) {
		this.cacheCli.remove(string);
	}
	
	

}
