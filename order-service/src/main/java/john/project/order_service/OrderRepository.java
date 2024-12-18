package john.project.order_service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query(value = "SELECT o.* FROM `orders` o " +
            "JOIN orderlist ol ON o.order_id = ol.order_id " +
            "WHERE ol.client_id = :clientId", nativeQuery = true)
	List<Order> findOrdersByClientId(@Param("clientId") Long clientId);
	
	@Modifying
    @Transactional
    @Query(value = "INSERT INTO orderlist (order_id, client_id) VALUES (:orderId, :clientId)", nativeQuery = true)
    void addOrderToClient(@Param("orderId") Long orderId, @Param("clientId") Long clientId);
	
	@Modifying
    @Transactional
    @Query(value = "DELETE FROM orderlist WHERE order_id=:orderId AND client_id=:clientId", nativeQuery = true)
	void deleteOrderToClient(@Param("orderId")Long id, @Param("clientId") Long clientID);
		
    @Query("SELECT ol.clientId FROM OrderList ol WHERE ol.orderId = :orderId")
    Long findClientIdByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Query("DELETE FROM OrderList ol WHERE ol.orderId = :orderId")
    void deleteByOrderId(@Param("orderId") Long orderId);

}