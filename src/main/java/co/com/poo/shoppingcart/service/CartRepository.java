package co.com.poo.shoppingcart.service;

import co.com.poo.shoppingcart.model.Cart;

/**
 * Representa un repositorio para almacenar y recuperar el carrito de compras.
 */
public interface CartRepository {
    /**
     *
     * @param cart El carrito de compras a guardar.
     */
    void saveCart(Cart cart);
    /**
     *
     * @return El carrito de compras guardado.
     */
    Cart getCart();

}
