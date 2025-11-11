package co.com.poo.shoppingcart.usecase;

import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.Order;
import co.com.poo.shoppingcart.service.CartRepository;
import java.util.HashMap;
import java.util.Map;

public class OrderUseCase {
    private final CartRepository cartRepository;
    private final Map<String, Order> orders; // Almacenamiento interno de órdenes

    // Constantes para la regla de descuento
    private static final double DISCOUNT_THRESHOLD = 100000.0; // Cantidad mínima para aplicar descuento
    private static final double DISCOUNT_PERCENTAGE = 0.05; // 5%

    public OrderUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        this.orders = new HashMap<>(); // Inicializamos el mapa de órdenes
    }

    /**
     * Crea una nueva orden a partir del carrito actual
     * Aplica descuento del 5% si el total es mayor a $100,000
     * @return La orden creada o null si el carrito está vacío
     */
    public Order createOrder() {
        Cart cart = cartRepository.getCart();

        // Verificar que el carrito no esté vacío
        if (cart.isEmpty()) {
            return null;
        }

        double subtotal = cart.getTotalAmount();
        double discount = calculateDiscount(subtotal);

        Order order = new Order(cart.getItems(), subtotal, discount);
        orders.put(order.getOrderId(), order); // Guardamos la orden en el mapa interno

        // No vaciamos el carrito automáticamente para permitir que el usuario lo haga explícitamente
        return order;
    }

    /**
     * Calcula el descuento según la regla de negocio:
     * Si el total es mayor a $100,000, se aplica un 5% de descuento.
     *
     * @param total El monto total del pedido
     * @return El valor del descuento a aplicar
     */
    private double calculateDiscount(double total) {
        if (total > DISCOUNT_THRESHOLD) {
            return total * DISCOUNT_PERCENTAGE;
        }
        return 0.0; // Sin descuento
    }

    /**
     * Obtiene una orden por su ID
     * @param orderId ID de la orden a buscar
     * @return La orden si existe, null si no se encuentra
     */
    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    /**
     * Vacía el carrito después de crear una orden
     * @return true si se vació correctamente, false si hubo algún error
     */
    public boolean clearCartAfterOrder() {
        Cart cart = cartRepository.getCart();
        cart.clear();
        cartRepository.saveCart(cart);
        return true;
    }
}