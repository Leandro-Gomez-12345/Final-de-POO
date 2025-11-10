package co.com.poo.shoppingcart.repositories;

import co.com.poo.shoppingcart.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
