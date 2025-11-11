package co.com.poo.shoppingcart.adapters;

import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.service.CartRepository;  // Interface del dominio
import co.com.poo.shoppingcart.services.CartService;    // Service JPA
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adaptador que implementa la interface del dominio (CartRepository)
 * usando el CartService que habla con JPA.
 */
@Component
public class JpaCartRepositoryAdapter implements CartRepository {

    @Autowired
    private CartService cartService;

    @Override
    public void saveCart(Cart cart) {
        cartService.saveCart(cart);
    }

    @Override
    public Cart getCart() {
        return cartService.getCurrentCart();
    }
}
