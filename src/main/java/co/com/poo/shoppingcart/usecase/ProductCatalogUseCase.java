package co.com.poo.shoppingcart.usecase;

import co.com.poo.shoppingcart.model.Product;
import co.com.poo.shoppingcart.service.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductCatalogUseCase {
    private final ProductRepository productRepository;

    public ProductCatalogUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<Product> getProductList() {
        return productRepository.loadProducts();
    }

    public Product getProductById(String id) {
        return productRepository.getProductById(Integer.parseInt(id));
    }

    // Nuevos métodos requeridos por el catálogo
    public List<Product> getByCategory(String category) {
        return productRepository.loadProducts().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Product> searchByPrice(Double minPrice, Double maxPrice) {
        return productRepository.loadProducts().stream()
                .filter(p -> {
                    double price = p.getPrice();
                    boolean minOk = (minPrice == null) || price >= minPrice;
                    boolean maxOk = (maxPrice == null) || price <= maxPrice;
                    return minOk && maxOk;
                })
                .collect(Collectors.toList());
    }
}
