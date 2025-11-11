package co.com.poo.shoppingcart.usecase;

import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.CartItem;
import co.com.poo.shoppingcart.model.Product;
import co.com.poo.shoppingcart.service.CartRepository;
import co.com.poo.shoppingcart.service.ProductRepository;
import co.com.poo.shoppingcart.services.CartService;  // ✅ NUEVO
import co.com.poo.shoppingcart.services.ProductService;  // ✅ NUEVO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component  // ✅ NUEVO - Para que Spring Boot lo maneje
public class ShoppingCartUseCase {

    @Autowired  // ✅ NUEVO - Inyección de dependencias
    private CartService cartService;

    @Autowired  // ✅ NUEVO
    private ProductService productService;

    public ShoppingCartUseCase(CartRepository cartRepository, ProductRepository productRepository) {
    }

    /**
     * Añade un producto al carrito de compras verificando su existencia y stock disponible
     */
    public boolean addProductToCart(String productId, Integer quantity) {
        try {
            Product product = productService.getProductById(Long.parseLong(productId));
            if (product != null && quantity > 0) {
                if (product.getStock() >= quantity) {
                    Cart cart = cartService.getCurrentCart();
                    cart.addItem(new CartItem(product, quantity));
                    cartService.saveCart(cart);
                    product.setStock(product.getStock() - quantity);
                    return true;
                }
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean updateQuantity(String productId, Integer quantity) {
        if (quantity <= 0) {
            return false;
        }

        Cart cart = cartService.getCurrentCart();
        CartItem existingItem = cart.findItem(productId);

        if (existingItem == null) {
            return false;
        }

        Product product = existingItem.getProduct();
        int currentQuantity = existingItem.getQuantity();
        int stockNeeded = quantity - currentQuantity;

        if (stockNeeded > 0 && product.getStock() < stockNeeded) {
            return false;
        }

        product.setStock(product.getStock() - stockNeeded);
        cart.updateItem(productId, quantity);
        cartService.saveCart(cart);
        return true;
    }

    public boolean removeItem(String productId) {
        Cart cart = cartService.getCurrentCart();
        CartItem item = cart.findItem(productId);

        if (item == null) {
            return false;
        }

        Product product = item.getProduct();
        product.setStock(product.getStock() + item.getQuantity());
        cart.removeItem(productId);
        cartService.saveCart(cart);
        return true;
    }

    public Cart viewCart() {
        return cartService.getCurrentCart();
    }

    public boolean clearCart() {
        Cart cart = cartService.getCurrentCart();

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }

        cart.clear();
        cartService.saveCart(cart);
        return true;
    }

    public boolean processOrder() {
        Cart cart = cartService.getCurrentCart();
        if (cart.getItems().isEmpty()) {
            return false;
        }
        return true;
    }
}
