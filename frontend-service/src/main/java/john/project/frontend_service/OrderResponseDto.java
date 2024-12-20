package john.project.frontend_service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderResponseDto {
	private Long id;
	private Long clientId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date orderDate;
    private String status;
    
	public OrderResponseDto(OrderResponseDto original) {
        this.orderDate = original.orderDate;
        this.clientId = original.clientId;
        this.status = original.status;
	}
	public OrderResponseDto() {}
	
	public OrderResponseDto(Object object, Date date2, String string, Object object2) {
		this.id = (Long) object;
		this.orderDate = date2;
        this.clientId = (Long) object2;
        this.status = string;
	}
	public OrderResponseDto(Object object, Date date2, String string) {
		this.id = (Long) object;
		this.orderDate = date2;
        this.status = string;
	}
	
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
