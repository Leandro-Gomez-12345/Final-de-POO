package co.com.poo.shoppingcart.controllers;

import co.com.poo.shoppingcart.dto.response.OrderItemResponseDTO;
import co.com.poo.shoppingcart.dto.response.OrderResponseDTO;
import co.com.poo.shoppingcart.exception.EmptyCartException;
import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.Order;
import co.com.poo.shoppingcart.services.CartService;
import co.com.poo.shoppingcart.services.OrderService;
import co.com.poo.shoppingcart.usecase.OrderUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para manejar las operaciones de órdenes/pedidos.
 * Expone endpoints REST para cerrar y cancelar pedidos.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderUseCase orderUseCase;

    /**
     * 1. Cerrar pedido (Checkout)
     * POST /api/orders/checkout
     *
     * Calcula el total, aplica descuentos si corresponde (>$100,000 = 5%),
     * genera el resumen del pedido y vacía el carrito.
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout() {
        // Obtener el carrito actual
        Cart cart = cartService.getCurrentCart();

        // Validar que el carrito no esté vacío
        if (cart.isEmpty()) {
            throw new EmptyCartException();
        }

        // Crear la orden usando el UseCase (aplica lógica de descuento)
        Order order = orderUseCase.createOrder();

        if (order == null) {
            throw new EmptyCartException();
        }

        // Guardar resumen en la base de datos
        orderService.saveOrderSummary("Default Customer", order);

        // Vaciar el carrito
        orderUseCase.clearCartAfterOrder();

        // Convertir a DTO y retornar
        OrderResponseDTO response = convertToOrderDTO(order);
        return ResponseEntity.ok(response);
    }

    /**
     * 2. Cancelar pedido (Vaciar carrito)
     * DELETE /api/orders/cancel
     *
     * Solo funciona si el pedido NO ha sido cerrado.
     * Vacía el carrito sin generar orden.
     */
    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelOrder() {
        Cart cart = cartService.getCurrentCart();

        if (cart.isEmpty()) {
            throw new EmptyCartException();
        }

        // Vaciar el carrito
        cartService.clearCart();

        return ResponseEntity.noContent().build();
    }

    //Metodo auxiliar para convertir el modelo Order a OrderResponseDTO

    private OrderResponseDTO convertToOrderDTO(Order order) {
        // Convertir items
        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getProduct().getId().longValue(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());

        // Convertir Date a LocalDateTime
        LocalDateTime orderDate = order.getDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return new OrderResponseDTO(
                order.getOrderId(),
                orderDate,
                items,
                order.getSubtotal(),
                order.getDiscount(),
                order.getFinalTotal()
        );
    }
}
