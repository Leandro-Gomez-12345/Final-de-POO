package co.com.poo.shoppingcart.service;

import co.com.poo.shoppingcart.model.Cart;

public class InMemoryCartManager implements CartRepository {

    private static final InMemoryCartManager INSTANCE = new InMemoryCartManager();
    private Cart cart;

    private InMemoryCartManager(){
        this.cart = new Cart();
    }

    public static InMemoryCartManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void saveCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public Cart getCart() {
        return cart;
    }
}
