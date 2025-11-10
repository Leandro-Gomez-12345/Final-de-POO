package co.com.poo.shoppingcart.controller;

import co.com.poo.shoppingcart.dto.request.CreateProductRequestDTO;
import co.com.poo.shoppingcart.dto.response.ProductResponseDTO;
import co.com.poo.shoppingcart.exception.ProductNotFoundException;
import co.com.poo.shoppingcart.model.Product;
import co.com.poo.shoppingcart.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar productos (CRUD + consultas).
 * Expone endpoints REST para el catálogo de productos.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * GET /api/products
     * Obtiene el catálogo completo de productos.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/{id}
     * Obtiene un producto por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return ResponseEntity.ok(toDTO(product));
    }

    /**
     * GET /api/products/category/{category}
     * Consulta productos por categoría.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductResponseDTO> response = products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/price/greater-than/{minPrice}
     * Consulta productos con precio mayor a un valor.
     */
    @GetMapping("/price/greater-than/{minPrice}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceGreaterThan(@PathVariable Double minPrice) {
        List<Product> products = productService.getProductsByPriceGreaterThan(minPrice);
        List<ProductResponseDTO> response = products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/price/range?min={minPrice}&max={maxPrice}
     * Consulta productos en un rango de precios.
     */
    @GetMapping("/price/range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<Product> products = productService.getProductsByPriceRange(min, max);
        List<ProductResponseDTO> response = products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/products
     * Crea un nuevo producto.
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody CreateProductRequestDTO request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(created));
    }

    /**
     * PUT /api/products/{id}
     * Actualiza un producto existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequestDTO request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product updated = productService.updateProduct(id, product);
        if (updated == null) {
            throw new ProductNotFoundException(id);
        }
        return ResponseEntity.ok(toDTO(updated));
    }

    /**
     * DELETE /api/products/{id}
     * Elimina un producto por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            throw new ProductNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }

    // ============ MÉTODO AUXILIAR ============

    /**
     * Convierte un Product (modelo) a ProductResponseDTO
     */
    private ProductResponseDTO toDTO(Product product) {
        return new ProductResponseDTO(
                product.getId().longValue(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock()
        );
    }
}
