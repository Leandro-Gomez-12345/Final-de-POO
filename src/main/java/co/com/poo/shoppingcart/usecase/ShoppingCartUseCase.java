package co.com.poo.shoppingcart.usecase;

import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.CartItem;
import co.com.poo.shoppingcart.model.Product;
import co.com.poo.shoppingcart.service.CartRepository;
import co.com.poo.shoppingcart.service.ProductRepository;
import org.springframework.stereotype.Component;

@Component  // Caso de uso como bean del dominio
public class ShoppingCartUseCase {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public ShoppingCartUseCase(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    /**
     * Añade un producto al carrito validando ID y cantidad (>0).
     * No gestiona stock (según requisitos).
     */
    public boolean addProductToCart(String productId, Integer quantity) {
        try {
            if (quantity == null || quantity <= 0) return false;

            Integer id = Integer.parseInt(productId);
            Product product = productRepository.getProductById(id);
            if (product == null) return false;

            Cart cart = cartRepository.getCart();
            cart.addItem(new CartItem(product, quantity));
            cartRepository.saveCart(cart);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean updateQuantity(String productId, Integer quantity) {
        if (quantity == null || quantity < 0) return false;

        Cart cart = cartRepository.getCart();
        // Si cantidad = 0, eliminar ítem
        if (quantity == 0) {
            CartItem existingItem = cart.findItem(productId);
            if (existingItem == null) return false;
            cart.removeItem(productId);
            cartRepository.saveCart(cart);
            return true;
        }

        CartItem existingItem = cart.findItem(productId);
        if (existingItem == null) return false;

        cart.updateItem(productId, quantity);
        cartRepository.saveCart(cart);
        return true;
    }

    public boolean removeItem(String productId) {
        Cart cart = cartRepository.getCart();
        CartItem item = cart.findItem(productId);
        if (item == null) return false;

        cart.removeItem(productId);
        cartRepository.saveCart(cart);
        return true;
    }

    public Cart viewCart() {
        return cartRepository.getCart();
    }

    public boolean clearCart() {
        Cart cart = cartRepository.getCart();
        cart.clear();
        cartRepository.saveCart(cart);
        return true;
    }

    // Este método queda como validación simple; el cierre real es del OrderUseCase
    public boolean processOrder() {
        Cart cart = cartRepository.getCart();
        return !cart.getItems().isEmpty();
    }
}
