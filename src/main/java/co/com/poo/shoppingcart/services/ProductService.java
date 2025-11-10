package co.com.poo.shoppingcart.services;

import co.com.poo.shoppingcart.entities.ProductEntity;
import co.com.poo.shoppingcart.repositories.ProductRepository;
import co.com.poo.shoppingcart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service que maneja la lógica de productos.
 * Actúa como puente entre los UseCases (dominio) y los Repositories (JPA).
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository; // JPA Repository

    /**
     * Obtiene todos los productos del catálogo desde la base de datos
     * @return Lista de productos (modelo de dominio)
     */
    public List<Product> getAllProducts() {
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }

    /**
     * Busca un producto por su ID en la base de datos
     * @param id ID del producto
     * @return El producto si existe, null si no se encuentra
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::entityToModel)
                .orElse(null);
    }

    /**
     * Busca productos por categoría
     * @param category Categoría a filtrar
     * @return Lista de productos de esa categoría
     */
    public List<Product> getProductsByCategory(String category) {
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream()
                .filter(entity -> entity.getDescription() != null &&
                        entity.getDescription().contains(category))
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos con precio mayor a un valor
     * @param minPrice Precio mínimo
     * @return Lista de productos filtrados
     */
    public List<Product> getProductsByPriceGreaterThan(Double minPrice) {
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream()
                .filter(entity -> entity.getPrice() > minPrice)
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos en un rango de precios
     * @param minPrice Precio mínimo
     * @param maxPrice Precio máximo
     * @return Lista de productos en ese rango
     */
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream()
                .filter(entity -> entity.getPrice() >= minPrice && entity.getPrice() <= maxPrice)
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo producto en la base de datos
     * @param product Producto a crear (modelo de dominio)
     * @return El producto creado con su ID asignado
     */
    public Product createProduct(Product product) {
        ProductEntity entity = modelToEntity(product);
        ProductEntity saved = productRepository.save(entity);
        return entityToModel(saved);
    }

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param product Datos actualizados del producto
     * @return El producto actualizado, o null si no existe
     */
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setName(product.getName());
                    existingEntity.setDescription(product.getCategory());
                    existingEntity.setPrice(product.getPrice());
                    existingEntity.setStock(product.getStock());
                    ProductEntity updated = productRepository.save(existingEntity);
                    return entityToModel(updated);
                })
                .orElse(null);
    }

    /**
     * Elimina un producto por su ID
     * @param id ID del producto a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ============ MÉTODOS DE CONVERSIÓN (Mappers) ============

    /**
     * Convierte una entidad JPA a modelo de dominio
     */
    private Product entityToModel(ProductEntity entity) {
        Product product = new Product();
        product.setId(entity.getId().intValue());
        product.setName(entity.getName());
        product.setCategory(entity.getDescription()); // Usando description como category
        product.setPrice(entity.getPrice());
        product.setStock(entity.getStock());
        return product;
    }

    /**
     * Convierte un modelo de dominio a entidad JPA
     */
    private ProductEntity modelToEntity(Product model) {
        ProductEntity entity = new ProductEntity();
        if (model.getId() != null) {
            entity.setId(model.getId().longValue());
        }
        entity.setName(model.getName());
        entity.setDescription(model.getCategory()); // Usando category como description
        entity.setPrice(model.getPrice());
        entity.setStock(model.getStock());
        return entity;
    }
}
