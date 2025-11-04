package co.com.poo.shoppingcart.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
    private List<CartItem> items = new ArrayList<>();
    private Double totalAmount = 0.0;

    public void addItem(CartItem item) {
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getProductId().equals(item.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
        calculateTotal();
    }

    public void removeItem(String productId) {
        items.removeIf(i -> i.getProductId().equals(Integer.parseInt(productId)));
        calculateTotal();
    }

    public void updateItem(String productId, Integer quantity) {
        if (quantity <= 0) {
            removeItem(productId);
            return;
        }
        for (CartItem item : items) {
            if (item.getProductId().equals(Integer.parseInt(productId))) {
                item.setQuantity(quantity);
                break;
            }
        }
        calculateTotal();
    }

    /**
     * Busca un item en el carrito por su ID de producto
     * @param productId ID del producto a buscar
     * @return El CartItem si existe, null si no se encuentra
     */
    public CartItem findItem(String productId) {
        try {
            Integer id = Integer.parseInt(productId);
            return items.stream()
                    .filter(item -> item.getProductId().equals(id))
                    .findFirst()
                    .orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Double calculateTotal() {
        totalAmount = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        return totalAmount;
    }

    public void clear() {
        items.clear();
        totalAmount = 0.0;
    }

    public Boolean isEmpty() {
        return items.isEmpty();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}
