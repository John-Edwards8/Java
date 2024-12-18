package john.project.order_service;

import java.util.Date;

public class OrderResponseDto {
	private Long id;
	private Long clientId;
    private Date orderDate;
    private String status;
    
	public OrderResponseDto(OrderResponseDto original) {
        this.orderDate = original.orderDate;
        this.status = original.status;
	}
	public OrderResponseDto() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

}
