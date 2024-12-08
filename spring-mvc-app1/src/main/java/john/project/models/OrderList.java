package john.project.models;

import java.util.Date;

public class OrderList {
    private Client client;
    private Order order;
    
    OrderList(OrderListBuilder builder) {
		this.client=builder.client;
		this.order=builder.order;
	}
    
	public Client getClient() {
		return client;
	}
	public Order getOrder() {
		return order;
	}
	
	public static class OrderListBuilder{
	    private Client client;
	    private Order order;

		public OrderListBuilder client(int id, String name,String surname,String patronymic,String phoneNumber, String email) {
			this.client = new Client
				.ClientBuilder()
				.id(id)
				.name(name)
				.surname(surname)
				.patronymic(patronymic)
				.phoneNumber(phoneNumber)
				.email(email)
				.build();
			return this;
		}

		public OrderListBuilder order(int id, Date date, String status) {
			this.order = new Order
				.OrderBuilder()
				.id(id)
				.date(date)
				.status(status)
				.build();
			return this;
		}
		
		public OrderList build(){
			return new OrderList(this);
		}

	}
}
