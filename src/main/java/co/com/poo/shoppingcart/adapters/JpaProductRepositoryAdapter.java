package co.com.poo.shoppingcart.adapters;

import co.com.poo.shoppingcart.model.Product;
import co.com.poo.shoppingcart.service.ProductRepository;  // Interface del dominio
import co.com.poo.shoppingcart.services.ProductService;    // Service JPA
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptador que implementa la interface del dominio (ProductRepository)
 * usando el ProductService que habla con JPA.
 *
 * Este es el "traductor" entre tu lógica de negocio y la tecnología JPA.
 */
@Component
public class JpaProductRepositoryAdapter implements ProductRepository {

    @Autowired
    private ProductService productService;

    @Override
    public List<Product> loadProducts() {
        return productService.getAllProducts();
    }

    @Override
    public Product getProductById(Integer id) {
        return productService.getProductById(id.longValue());
    }
}
