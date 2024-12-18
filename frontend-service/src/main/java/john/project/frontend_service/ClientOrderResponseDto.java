package john.project.frontend_service;

public class ClientOrderResponseDto {
    private ClientResponseDto client;
    private OrderResponseDto order;

	public ClientOrderResponseDto(OrderResponseDto order, ClientResponseDto client) {
		this.client = client;
		this.order = order;
	}

	public ClientResponseDto getClient() {
		return client;
	}

	public void setClient(ClientResponseDto client) {
		this.client = client;
	}

	public OrderResponseDto getOrder() {
		return order;
	}

	public void setOrder(OrderResponseDto order) {
		this.order = order;
	}
	
}
