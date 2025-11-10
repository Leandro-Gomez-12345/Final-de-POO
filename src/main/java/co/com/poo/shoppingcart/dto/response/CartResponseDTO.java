package co.com.poo.shoppingcart.dto.response;

import java.util.List;

/**
 * DTO para devolver el contenido del carrito al cliente.
 */
public class CartResponseDTO {

    private List<CartItemResponseDTO> items;
    private Double totalAmount;
    private Boolean isEmpty;

    // Constructores
    public CartResponseDTO() {}

    public CartResponseDTO(List<CartItemResponseDTO> items, Double totalAmount, Boolean isEmpty) {
        this.items = items;
        this.totalAmount = totalAmount;
        this.isEmpty = isEmpty;
    }

    // Getters y Setters
    public List<CartItemResponseDTO> getItems() { return items; }
    public void setItems(List<CartItemResponseDTO> items) { this.items = items; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Boolean getIsEmpty() { return isEmpty; }
    public void setIsEmpty(Boolean isEmpty) { this.isEmpty = isEmpty; }
}
