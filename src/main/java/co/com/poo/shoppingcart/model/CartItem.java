package co.com.poo.shoppingcart.model;

public class CartItem {
    private Product product;
    private Integer quantity;
    private Double subtotal;

    public CartItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.subtotal = calculateSubtotal();
    }

    public Double calculateSubtotal() {
        if (product == null || quantity == null) return 0.0;
        subtotal = product.getPrice() * quantity;
        return subtotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        calculateSubtotal();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }

    public Integer getProductId() {
        return product.getId();
    }

    public Double getPrice() {
        return product.getPrice();
    }
}
