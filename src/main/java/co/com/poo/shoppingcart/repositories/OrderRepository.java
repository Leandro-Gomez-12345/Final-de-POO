package co.com.poo.shoppingcart.repositories;

import co.com.poo.shoppingcart.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
