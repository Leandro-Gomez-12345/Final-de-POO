package co.com.poo.shoppingcart.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long id) {
        super("Producto con ID " + id + " no encontrado");
    }
}