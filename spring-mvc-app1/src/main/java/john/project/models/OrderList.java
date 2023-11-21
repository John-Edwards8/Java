package john.project.models;

import java.util.Date;

public class OrderList {
    private Client client;
    private Order order;
    
	public Client getClient() {
		return client;
	}

	public Order getOrder() {
		return order;
	}
	public void setClient(int newId, String name,String surname,String patronymic,String phoneNumber, String email) {
		client = new Client();
		client.setId(newId);
		client.setName(name);	
		client.setSurname(surname);		
		client.setPatronymic(patronymic);
		client.setPhoneNumber(phoneNumber);
		client.setEmail(email);
	}

	public void setOrder(int newId, Date date, String status) {
		order = new Order();
		order.setId(newId);
		order.setDate(date);
		order.setStatus(status);
	}
}
