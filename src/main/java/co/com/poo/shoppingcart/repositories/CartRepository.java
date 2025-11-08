package co.com.poo.shoppingcart.repositories;

import co.com.poo.shoppingcart.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}
