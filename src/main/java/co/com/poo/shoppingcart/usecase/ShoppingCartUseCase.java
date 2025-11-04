package co.com.poo.shoppingcart.usecase;

import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.CartItem;
import co.com.poo.shoppingcart.model.Product;
import co.com.poo.shoppingcart.service.CartRepository;
import co.com.poo.shoppingcart.service.ProductRepository;

public class ShoppingCartUseCase {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public ShoppingCartUseCase(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    /**
     * Añade un producto al carrito de compras verificando su existencia y stock disponible
     * @param productId ID del producto a añadir
     * @param quantity Cantidad del producto a añadir
     * @return true si el producto fue añadido correctamente, false si no existe o no hay stock suficiente
     */
    public boolean addProductToCart(String productId, Integer quantity) {
        try {
            Product product = productRepository.getProductById(Integer.parseInt(productId));
            if (product != null && quantity > 0) {
                // Verifica stock disponible
                if (product.getStock() >= quantity) {
                    Cart cart = cartRepository.getCart();
                    cart.addItem(new CartItem(product, quantity));
                    cartRepository.saveCart(cart);
                    // Actualiza el stock del producto
                    product.setStock(product.getStock() - quantity);
                    return true;
                }
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Actualiza la cantidad de un producto en el carrito
     * @param productId ID del producto a actualizar
     * @param quantity Nueva cantidad del producto
     * @return true si la cantidad fue actualizada correctamente, false si el producto no existe en el carrito
     * o no hay stock suficiente
     */
    public boolean updateQuantity(String productId, Integer quantity) {
        if (quantity <= 0) {
            return false;
        }
        
        Cart cart = cartRepository.getCart();
        CartItem existingItem = cart.findItem(productId);
        
        if (existingItem == null) {
            return false;
        }
        
        Product product = existingItem.getProduct();
        int currentQuantity = existingItem.getQuantity();
        int stockNeeded = quantity - currentQuantity;
        
        // Si necesitamos más unidades, verificar stock
        if (stockNeeded > 0 && product.getStock() < stockNeeded) {
            return false;
        }
        
        // Actualizar stock
        product.setStock(product.getStock() - stockNeeded);
        
        // Actualizar carrito
        cart.updateItem(productId, quantity);
        cartRepository.saveCart(cart);
        return true;
    }

    /**
     * Elimina un producto del carrito y devuelve su stock
     * @param productId ID del producto a eliminar
     * @return true si el producto fue eliminado correctamente, false si el producto no existe en el carrito
     */
    public boolean removeItem(String productId) {
        Cart cart = cartRepository.getCart();
        CartItem item = cart.findItem(productId);
        
        if (item == null) {
            return false;
        }
        
        // Devolver el stock
        Product product = item.getProduct();
        product.setStock(product.getStock() + item.getQuantity());
        
        // Eliminar del carrito
        cart.removeItem(productId);
        cartRepository.saveCart(cart);
        return true;
    }

    /**
     * Obtiene el carrito actual
     * @return El carrito de compras actual
     */
    public Cart viewCart() {
        return cartRepository.getCart();
    }

    /**
     * Limpia todos los productos del carrito y devuelve su stock
     * @return true si el carrito fue limpiado correctamente
     */
    public boolean clearCart() {
        Cart cart = cartRepository.getCart();
        
        // Devolver el stock de todos los productos
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }
        
        cart.clear();
        cartRepository.saveCart(cart);
        return true;
    }

    /**
     * Procesa el pedido actual y permite seguir agregando productos al carrito para nuevos pedidos.
     * El carrito no se borra automáticamente después de procesar la orden.
     * @return true si el pedido se procesó correctamente, false si hubo algún error.
     */
    public boolean processOrder() {
        // Verificar que el carrito no esté vacío
        Cart cart = cartRepository.getCart();
        if (cart.getItems().isEmpty()) {
            return false;
        }
        
        // Aquí iría la lógica para procesar el pedido
        return true;
    }
}