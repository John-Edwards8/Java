package john.project.order_service;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;
    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    @Column(name = "order_status", nullable = false)
    private String status;
    
    public Order() {}
    
    public Order(Date orderDate, String status) {
    	this.orderDate = orderDate;
    	this.status = status;
	}
	public Order(Object l, Date date, String string) {
		this.order_id = (Long)l;
		this.orderDate = date;
    	this.status = string;
	}

	public Long getOrderId() {
        return order_id;
    }
    public void setOrderId(Long order_id) {
        this.order_id = order_id;
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

}