package co.com.poo.shoppingcart.services;

import co.com.poo.shoppingcart.entities.CartEntity;
import co.com.poo.shoppingcart.entities.CartItemEntity;
import co.com.poo.shoppingcart.entities.ProductEntity;
import co.com.poo.shoppingcart.repositories.CartRepository;
import co.com.poo.shoppingcart.repositories.ProductRepository;
import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.CartItem;
import co.com.poo.shoppingcart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service que maneja la lógica del carrito de compras
 */
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Obtiene o crea el carrito actual
     * Por simplicidad, manejamos un solo carrito (ID = 1)
     */
    public Cart getCurrentCart() {
        CartEntity entity = cartRepository.findById(1L)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setCustomerName("Default Customer");
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });
        return entityToModel(entity);
    }

    /**
     * Guarda el carrito en la base de datos
     */
    public void saveCart(Cart cart) {
        CartEntity entity = modelToEntity(cart);
        cartRepository.save(entity);
    }

    /**
     * Agrega un producto al carrito
     */
    public Cart addProductToCart(Long productId, Integer quantity) {
        CartEntity cartEntity = cartRepository.findById(1L)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setCustomerName("Default Customer");
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Buscar si el producto ya está en el carrito
        CartItemEntity existingItem = cartEntity.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItemEntity newItem = new CartItemEntity();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cartEntity);
            cartEntity.getItems().add(newItem);
        }

        cartRepository.save(cartEntity);
        return entityToModel(cartEntity);
    }

    /**
     * Limpia el carrito
     */
    public void clearCart() {
        CartEntity cartEntity = cartRepository.findById(1L).orElse(null);
        if (cartEntity != null) {
            cartEntity.getItems().clear();
            cartRepository.save(cartEntity);
        }
    }

    // ============ MÉTODOS DE CONVERSIÓN ============

    private Cart entityToModel(CartEntity entity) {
        Cart cart = new Cart();
        List<CartItem> items = entity.getItems().stream()
                .map(itemEntity -> {
                    ProductEntity p = itemEntity.getProduct();
                    Product product = new Product(
                            p.getId().intValue(),
                            p.getName(),
                            p.getDescription(),  // description mapeada como category
                            p.getPrice(),
                            p.getStock()
                    );
                    return new CartItem(product, itemEntity.getQuantity());
                })
                .collect(Collectors.toList());

        for (CartItem item : items) {
            cart.addItem(item);
        }
        return cart;
    }

    private CartEntity modelToEntity(Cart model) {
        CartEntity entity = cartRepository.findById(1L).orElseGet(CartEntity::new);
        entity.setCustomerName("Default Customer");

        List<CartItemEntity> itemEntities = model.getItems().stream()
                .map(item -> {
                    CartItemEntity itemEntity = new CartItemEntity();
                    ProductEntity product = productRepository.findById(item.getProduct().getId().longValue())
                            .orElseThrow();
                    itemEntity.setProduct(product);
                    itemEntity.setQuantity(item.getQuantity());
                    itemEntity.setCart(entity);
                    return itemEntity;
                })
                .collect(Collectors.toList());

        entity.setItems(itemEntities);
        return entity;
    }
}
