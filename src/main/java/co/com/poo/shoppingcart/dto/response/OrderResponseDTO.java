package co.com.poo.shoppingcart.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para devolver el resumen de una orden cerrada (checkout).
 */
public class OrderResponseDTO {

    private String orderId;
    private LocalDateTime date;
    private List<OrderItemResponseDTO> items;
    private Double subtotal;
    private Double discount;
    private Double finalTotal;

    // Constructores
    public OrderResponseDTO() {}

    public OrderResponseDTO(String orderId, LocalDateTime date, List<OrderItemResponseDTO> items,
                            Double subtotal, Double discount, Double finalTotal) {
        this.orderId = orderId;
        this.date = date;
        this.items = items;
        this.subtotal = subtotal;
        this.discount = discount;
        this.finalTotal = finalTotal;
    }

    // Getters y Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public List<OrderItemResponseDTO> getItems() { return items; }
    public void setItems(List<OrderItemResponseDTO> items) { this.items = items; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public Double getFinalTotal() { return finalTotal; }
    public void setFinalTotal(Double finalTotal) { this.finalTotal = finalTotal; }
}
