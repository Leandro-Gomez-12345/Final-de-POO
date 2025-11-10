package co.com.poo.shoppingcart.controller;

import co.com.poo.shoppingcart.dto.response.OrderItemResponseDTO;
import co.com.poo.shoppingcart.dto.response.OrderResponseDTO;
import co.com.poo.shoppingcart.exception.EmptyCartException;
import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.CartItem;
import co.com.poo.shoppingcart.model.Order;
import co.com.poo.shoppingcart.services.CartService;
import co.com.poo.shoppingcart.usecase.OrderUseCase;
import co.com.poo.shoppingcart.usecase.ShoppingCartUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar pedidos (órdenes).
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderUseCase orderUseCase;

    @Autowired
    private ShoppingCartUseCase shoppingCartUseCase;

    @Autowired
    private CartService cartService;

    /**
     * POST /api/orders/checkout
     * Cierra el pedido actual (aplica descuento si corresponde y genera resumen).
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout() {
        Cart cart = cartService.getCurrentCart();

        if (cart.isEmpty()) {
            throw new EmptyCartException();
        }

        // Crear la orden
        Order order = orderUseCase.createOrder();

        if (order == null) {
            throw new EmptyCartException();
        }

        // Vaciar el carrito después de crear la orden
        orderUseCase.clearCartAfterOrder();

        // Convertir a DTO
        OrderResponseDTO response = toDTO(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/orders/{orderId}
     * Consulta una orden por su ID.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable String orderId) {
        Order order = orderUseCase.getOrder(orderId);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toDTO(order));
    }

    /**
     * DELETE /api/orders/cancel
     * Cancela el pedido actual (vacía el carrito si no se ha cerrado).
     */
    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelOrder() {
        shoppingCartUseCase.clearCart();
        return ResponseEntity.noContent().build();
    }

    // ============ MÉTODO AUXILIAR ============

    /**
     * Convierte una Order (modelo) a OrderResponseDTO
     */
    private OrderResponseDTO toDTO(Order order) {
        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getProduct().getId().longValue(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        item.calculateSubtotal()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getOrderId(),
                order.getDate(),
                items,
                order.getSubtotal(),
                order.getDiscount(),
                order.getFinalTotal()
        );
    }
}
