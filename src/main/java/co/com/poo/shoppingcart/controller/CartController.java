package co.com.poo.shoppingcart.controller;

import co.com.poo.shoppingcart.dto.request.AddToCartRequestDTO;
import co.com.poo.shoppingcart.dto.request.UpdateQuantityRequestDTO;
import co.com.poo.shoppingcart.dto.response.CartItemResponseDTO;
import co.com.poo.shoppingcart.dto.response.CartResponseDTO;
import co.com.poo.shoppingcart.exception.InvalidQuantityException;
import co.com.poo.shoppingcart.exception.ProductNotFoundException;
import co.com.poo.shoppingcart.model.Cart;
import co.com.poo.shoppingcart.model.CartItem;
import co.com.poo.shoppingcart.services.CartService;
import co.com.poo.shoppingcart.usecase.ShoppingCartUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gestionar el carrito de compras.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ShoppingCartUseCase shoppingCartUseCase;

    /**
     * GET /api/cart
     * Obtiene el contenido actual del carrito.
     */
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {
        Cart cart = cartService.getCurrentCart();
        return ResponseEntity.ok(toDTO(cart));
    }

    /**
     * POST /api/cart/add
     * Agrega un producto al carrito.
     */
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addProductToCart(@Valid @RequestBody AddToCartRequestDTO request) {
        if (request.getQuantity() <= 0) {
            throw new InvalidQuantityException();
        }

        boolean success = shoppingCartUseCase.addProductToCart(
                request.getProductId().toString(),
                request.getQuantity()
        );

        if (!success) {
            throw new ProductNotFoundException(request.getProductId());
        }

        Cart cart = cartService.getCurrentCart();
        return ResponseEntity.ok(toDTO(cart));
    }

    /**
     * PUT /api/cart/update
     * Actualiza la cantidad de un producto en el carrito.
     */
    @PutMapping("/update")
    public ResponseEntity<CartResponseDTO> updateQuantity(@Valid @RequestBody UpdateQuantityRequestDTO request) {
        if (request.getQuantity() <= 0) {
            throw new InvalidQuantityException();
        }

        boolean success = shoppingCartUseCase.updateQuantity(
                request.getProductId().toString(),
                request.getQuantity()
        );

        if (!success) {
            throw new ProductNotFoundException(request.getProductId());
        }

        Cart cart = cartService.getCurrentCart();
        return ResponseEntity.ok(toDTO(cart));
    }

    /**
     * DELETE /api/cart/remove/{productId}
     * Elimina un producto del carrito.
     */
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponseDTO> removeProduct(@PathVariable Long productId) {
        boolean success = shoppingCartUseCase.removeItem(productId.toString());

        if (!success) {
            throw new ProductNotFoundException(productId);
        }

        Cart cart = cartService.getCurrentCart();
        return ResponseEntity.ok(toDTO(cart));
    }

    /**
     * DELETE /api/cart/clear
     * Vacía el carrito completamente.
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        shoppingCartUseCase.clearCart();
        return ResponseEntity.noContent().build();
    }

    // ============ MÉTODO AUXILIAR ============

    /**
     * Convierte un Cart (modelo) a CartResponseDTO
     */
    private CartResponseDTO toDTO(Cart cart) {
        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(item -> new CartItemResponseDTO(
                        item.getProduct().getId().longValue(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.calculateSubtotal()
                ))
                .collect(Collectors.toList());

        return new CartResponseDTO(items, cart.getTotalAmount(), cart.isEmpty());
    }
}
