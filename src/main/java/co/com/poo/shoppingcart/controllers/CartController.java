package co.com.poo.shoppingcart.controllers;

import co.com.poo.shoppingcart.dto.request.AddToCartRequestDTO;
import co.com.poo.shoppingcart.dto.request.UpdateQuantityRequestDTO;
import co.com.poo.shoppingcart.dto.response.CartItemResponseDTO;
import co.com.poo.shoppingcart.dto.response.CartResponseDTO;
import co.com.poo.shoppingcart.exception.InvalidQuantityException;
import co.com.poo.shoppingcart.exception.ProductNotFoundException;
import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.CartItem;
import co.com.poo.shoppingcart.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para manejar todas las operaciones del carrito de compras.
 * Expone endpoints REST para agregar, ver, actualizar y eliminar items del carrito.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 1. Ver el carrito de compras actual
     * GET /api/cart
     */
    @GetMapping
    public ResponseEntity<CartResponseDTO> viewCart() {
        Cart cart = cartService.getCurrentCart();
        CartResponseDTO response = convertToCartDTO(cart);
        return ResponseEntity.ok(response);
    }

    /**
     * 2. Agregar un producto al carrito
     * POST /api/cart/add
     */
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addProductToCart(
            @Valid @RequestBody AddToCartRequestDTO request) {

        // Validar cantidad
        if (request.getQuantity() <= 0) {
            throw new InvalidQuantityException("La cantidad debe ser mayor a 0");
        }

        try {
            Cart cart = cartService.addProductToCart(request.getProductId(), request.getQuantity());
            CartResponseDTO response = convertToCartDTO(cart);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new ProductNotFoundException("No se pudo agregar el producto. Verifique el ID.");
        }
    }

    /**
     * 3. Actualizar la cantidad de un producto en el carrito
     * PUT /api/cart/update
     */
    @PutMapping("/update")
    public ResponseEntity<CartResponseDTO> updateQuantity(
            @Valid @RequestBody UpdateQuantityRequestDTO request) {

        // Validar cantidad
        if (request.getQuantity() <= 0) {
            throw new InvalidQuantityException("La cantidad debe ser mayor a 0");
        }

        Cart cart = cartService.getCurrentCart();

        // Buscar el item en el carrito
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().longValue() == request.getProductId())
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado en el carrito"));

        // Actualizar cantidad
        item.setQuantity(request.getQuantity());
        cartService.saveCart(cart);

        CartResponseDTO response = convertToCartDTO(cart);
        return ResponseEntity.ok(response);
    }

    /**
     * 4. Eliminar un producto del carrito
     * DELETE /api/cart/remove/{productId}
     */
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponseDTO> removeItem(@PathVariable Long productId) {
        Cart cart = cartService.getCurrentCart();

        // Buscar y eliminar el item
        boolean removed = cart.getItems().removeIf(
                item -> item.getProduct().getId().longValue() == productId
        );

        if (!removed) {
            throw new ProductNotFoundException("Producto no encontrado en el carrito");
        }

        cart.calculateTotal();
        cartService.saveCart(cart);

        CartResponseDTO response = convertToCartDTO(cart);
        return ResponseEntity.ok(response);
    }

    /**
     * 5. Vaciar el carrito completamente
     * DELETE /api/cart/clear
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }

    // Metodo auxiliar para convertir el modelo Cart a CartResponseDTO

    private CartResponseDTO convertToCartDTO(Cart cart) {
        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(item -> new CartItemResponseDTO(
                        item.getProduct().getId().longValue(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new CartResponseDTO(
                items,
                cart.getTotalAmount(),
                cart.isEmpty()
        );
    }
}
