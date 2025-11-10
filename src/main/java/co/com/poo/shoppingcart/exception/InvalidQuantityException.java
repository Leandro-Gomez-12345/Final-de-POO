package co.com.poo.shoppingcart.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(String message) {
        super(message);
    }

    public InvalidQuantityException() {
        super("La cantidad debe ser mayor a 0");
    }
}
