package co.com.poo.shoppingcart.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("No se puede procesar un pedido con el carrito vacío");
    }
}
