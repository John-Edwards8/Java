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
import john.project.models.Order;
import john.project.models.OrderList;

@Component
public class PostgreSQLDAO implements IDAO {
	private static final String URL = "jdbc:postgresql://localhost:5432/userdb";
	private static final String USER = "postgres";
	private static final String PASSWORD = "1111";

	private static Connection con;
    private HashMap<String, Order> cacheOrder;
    private HashMap<String, Client> cacheCli;

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
            	Client person = new Client();
            	
            	person.setId(resultSet.getInt("client_id"));;
                person.setName(resultSet.getString("client_first_name"));
                person.setSurname(resultSet.getString("client_second_name"));
                person.setPatronymic(resultSet.getString("client_patronymic"));
                person.setPhoneNumber(resultSet.getString("client_phone_number"));
                person.setEmail(resultSet.getString("client_email"));

                clients.add(person);
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
	        String SQL = "INSERT INTO indent (indent_date, indent_status) VALUES('" + formatter.format(order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + "', '" + order.getStatus() + "')";
		
	        con.createStatement().executeUpdate(SQL);
	    } catch (SQLException throwables) {
	        throwables.printStackTrace();
	    }
    }
    
    @Override
    public void updateOrder(int id, Order order) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	 try {
	        PreparedStatement preparedStatement = con.prepareStatement("UPDATE indent SET indent_status = ?, indent_date='" + formatter.format(order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + "' WHERE indent_id = ?");
	
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
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM indent WHERE indent_id=?");
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
            	client =  new Client();
                client.setId(resultSet.getInt("client_id"));;
                client.setName(resultSet.getString("client_first_name"));
                client.setSurname(resultSet.getString("client_second_name"));
                client.setPatronymic(resultSet.getString("client_patronymic"));
                client.setPhoneNumber(resultSet.getString("client_phone_number"));
                client.setEmail(resultSet.getString("client_email"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return client;
    }
    
    public Order getOrder(int id) {
    	Order order = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM indent WHERE indent_id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
	            order =  new Order();
	            order.setId(resultSet.getInt("indent_id"));
	            order.setDate(resultSet.getDate("indent_date"));
	            order.setStatus(resultSet.getString("indent_status"));
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
            String SQL = "SELECT * FROM indent";
            ResultSet resultSet = con.createStatement().executeQuery(SQL);

            while(resultSet.next()) {
            	Order ord = new Order();
            	
            	ord.setId(resultSet.getInt("indent_id"));
            	ord.setDate(resultSet.getDate("indent_date"));
            	ord.setStatus(resultSet.getString("indent_status"));

                orders.add(ord);
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
            String SQL = "INSERT INTO orderlist (orderid, clientid) VALUES ((SELECT currval(pg_get_serial_sequence('indent', 'indent_id')))," + clientId + ");";
            con.createStatement().executeUpdate(SQL);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	}

    @Override
    public void updateOrderList(Order order, int clientID) {
        this.updateOrder(order.getId(), order);

        try {
        	PreparedStatement preparedStatement = con.prepareStatement("UPDATE orderlist SET orderID=?, clientID=? WHERE orderID=?;");
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
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM orderlist WHERE orderID=? AND clientID=?;");
            preparedStatement.setInt(1, orderID);
            preparedStatement.setInt(2, clientID);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public OrderList getOrderList(int id) {
    	OrderList order = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM clients LEFT JOIN (orderlist LEFT JOIN indent ON (orderid = indent_id)) ON(client_id=clientid) WHERE indent_id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            
            order = new OrderList();
            order.setClient(resultSet.getInt("client_id"), resultSet.getString("client_first_name"), resultSet.getString("client_second_name"),
        			resultSet.getString("client_patronymic"), resultSet.getString("client_phone_number"), resultSet.getString("client_email"));
            order.setOrder(resultSet.getInt("indent_id"), resultSet.getDate("indent_date"), resultSet.getString("indent_status"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return order;
    }

    @Override
    public List<OrderList> getAllOrderLists() {
    	List<OrderList> orderLists = new ArrayList<>();

        try {
            String SQL = "SELECT * FROM clients LEFT JOIN orderlist ON client_id = clientid LEFT JOIN indent ON orderid = indent_id WHERE orderid IS NOT NULL;";
            ResultSet resultSet = con.createStatement().executeQuery(SQL);
            while(resultSet.next()) {
            	OrderList ord = new OrderList();

            	ord.setClient(resultSet.getInt("client_id"), resultSet.getString("client_first_name"), resultSet.getString("client_second_name"),
            			resultSet.getString("client_patronymic"), resultSet.getString("client_phone_number"), resultSet.getString("client_email"));
            	ord.setOrder(resultSet.getInt("indent_id"), resultSet.getDate("indent_date"), resultSet.getString("indent_status"));
				
            	orderLists.add(ord);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orderLists;
    }
    
    @Override
	public boolean authenticate(String username, String password2) {
		String uname;
		String pass;
		
		try {
            String SQL = "SELECT client_email, client_password FROM clients;";
            ResultSet resultSet = con.createStatement().executeQuery(SQL);
            while(resultSet.next()) {
            	uname = resultSet.getString("client_email");
            	pass = resultSet.getString("client_password");
				
            	if(uname == username && pass == password2) {
            		return true;
            	}
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

		return false;
	}

	@Override
	public HashMap<String, Order> getCache() {
		return this.cacheOrder;
	}
	
	@Override
	public HashMap<String, Client> getCli() {
		return this.cacheCli;
	}

	public void save(String key, Order obj) {
		cacheOrder.put(key, obj);
	}
	
	public Order get(String key) {
		return this.cacheOrder.get(key);
	}

	public void deleteOrder(String string) {
		this.cacheOrder.remove(string);
	}

	public void save(String key, Client client) {
		cacheCli.put(key, client);
		
	}
	
	public Client getLast(String key) {
		return this.cacheCli.get(key);
	}
	
	public void deleteClient(String string) {
		this.cacheCli.remove(string);
	}
	
	

}
