package co.com.poo.shoppingcart.usecase;

import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.Order;
import co.com.poo.shoppingcart.service.CartRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderUseCase {
    private final CartRepository cartRepository;
    private final Map<String, Order> orders; // Almacenamiento interno de órdenes

    private static final double DISCOUNT_THRESHOLD = 100000.0;
    private static final double DISCOUNT_PERCENTAGE = 0.05;

    public OrderUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        this.orders = new HashMap<>();
    }

    public Order createOrder() {
        Cart cart = cartRepository.getCart();
        if (cart.isEmpty()) return null;

        double subtotal = cart.getTotalAmount();
        double discount = calculateDiscount(subtotal);

        Order order = new Order(cart.getItems(), subtotal, discount);
        orders.put(order.getOrderId(), order);

        return order;
    }

    private double calculateDiscount(double total) {
        if (total > DISCOUNT_THRESHOLD) {
            return total * DISCOUNT_PERCENTAGE;
        }
        return 0.0; // Sin descuento
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public boolean clearCartAfterOrder() {
        Cart cart = cartRepository.getCart();
        cart.clear();
        cartRepository.saveCart(cart);
        return true;
    }
}