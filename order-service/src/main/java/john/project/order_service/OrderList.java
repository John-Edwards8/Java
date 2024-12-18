package john.project.order_service;

import jakarta.persistence.*;

@Entity
@Table(name = "orderlist")
public class OrderList {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "order_id", nullable = false)
    private Long orderId;

	@Column(name = "client_id", nullable = false)
	private Long clientId;

    public OrderList() {}

    public OrderList(Long orderId, Long clientId) {
        this.orderId = orderId;
        this.clientId = clientId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
