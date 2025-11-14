package co.com.poo.shoppingcart.controllers;

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
 * Controller para manejar todas las operaciones relacionadas con productos.
 * Expone endpoints REST para el CRUD completo y consultas especializadas.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 1. Ver catálogo completo de productos
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * 2. Obtener un producto por ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return ResponseEntity.ok(convertToDTO(product));
    }

    /**
     * 3. Crear un nuevo producto
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody CreateProductRequestDTO request) {

        Product product = new Product(
                null,
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock()
        );

        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(created));
    }

    /**
     * 4. Actualizar un producto existente
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequestDTO request) {

        Product product = new Product(
                id.intValue(),
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock()
        );

        Product updated = productService.updateProduct(id, product);
        if (updated == null) {
            throw new ProductNotFoundException(id);
        }

        return ResponseEntity.ok(convertToDTO(updated));
    }

    /**
     * 5. Eliminar un producto
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            throw new ProductNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * 6. Consultar productos por categoría
     * GET /api/products/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable String category) {

        List<Product> products = productService.getProductsByCategory(category);
        List<ProductResponseDTO> response = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * 7. Consultar productos con precio mayor a X
     * GET /api/products/price/greater-than?minPrice=100000
     */
    @GetMapping("/price/greater-than")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceGreaterThan(
            @RequestParam Double minPrice) {

        List<Product> products = productService.getProductsByPriceGreaterThan(minPrice);
        List<ProductResponseDTO> response = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * 8. Consultar productos en un rango de precios
     * GET /api/products/price/range?minPrice=50000&maxPrice=200000
     */
    @GetMapping("/price/range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {

        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        List<ProductResponseDTO> response = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    //Metodo auxiliar para convertir a DTO

    private ProductResponseDTO convertToDTO(Product product) {
        return new ProductResponseDTO(
                product.getId().longValue(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock()
        );
    }
}