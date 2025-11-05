package co.com.poo.shoppingcart.application;

import co.com.poo.shoppingcart.service.CartRepository;
import co.com.poo.shoppingcart.service.InMemoryCartManager;
import co.com.poo.shoppingcart.service.ProductRepository;
import co.com.poo.shoppingcart.service.FileProductManager;
import co.com.poo.shoppingcart.usecase.ShoppingCartUseCase;
import co.com.poo.shoppingcart.usecase.ProductCatalogUseCase;
import co.com.poo.shoppingcart.usecase.OrderUseCase;
import co.com.poo.shoppingcart.model.Order;
import co.com.poo.shoppingcart.model.Product;

import java.util.Scanner;

public class ShoppingCartApp {
    public static void main(String[] args) {
        // Inicializar repositorios
        CartRepository cartRepository = InMemoryCartManager.getInstance();
        ProductRepository productRepository = FileProductManager.getInstance();
        
        // Inicializar casos de uso
        ShoppingCartUseCase shoppingCartUseCase = new ShoppingCartUseCase(cartRepository, productRepository);
        ProductCatalogUseCase productCatalogUseCase = new ProductCatalogUseCase(productRepository);
        OrderUseCase orderUseCase = new OrderUseCase(cartRepository);
        
        // Iniciar la interfaz de consola
        startConsoleInterface(shoppingCartUseCase, productCatalogUseCase, orderUseCase);
    }
    
    /**
     * Inicia la interfaz de consola para interactuar con el sistema de carrito de compras
     * @param shoppingCartUseCase Caso de uso para operaciones del carrito
     * @param productCatalogUseCase Caso de uso para operaciones del catálogo de productos
     * @param orderUseCase Caso de uso para operaciones de órdenes
     */
    private static void startConsoleInterface(ShoppingCartUseCase shoppingCartUseCase, 
                                             ProductCatalogUseCase productCatalogUseCase,
                                             OrderUseCase orderUseCase) {
        Scanner scanner = new Scanner(System.in);
        boolean activo = true;
        String currentOrderId = null; // Para almacenar el ID de la orden actual
        boolean orderClosed = false; // Para controlar si ya se cerró el pedido
        
        System.out.println("Bienvenido al Carrito de Compras");
        
        while (activo) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Ver catálogo de productos");
            System.out.println("2. Añadir producto al carrito");
            System.out.println("3. Ver carrito de compras");
            System.out.println("4. Actualizar cantidad de un producto");
            System.out.println("5. Eliminar producto del carrito");
            System.out.println("6. Cerrar pedido (checkout)");
            System.out.println("7. Cancelar pedido (vaciar carrito)");
            System.out.println("8. Ver detalles de la orden");
            System.out.println("9. Salir");

            int opcion = 0;
            while (true){
                try {
                    System.out.print("Seleccione una opción: ");
                    opcion = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    break; // Salir del bucle si la entrada es válida
                } catch (Exception e) {
                    System.out.println("Entrada inválida. Por favor ingrese un número.");
                    scanner.nextLine();
                }
            }
            
            switch (opcion) {
                case 1:
                    // Ver catálogo de productos
                    System.out.println("\n=== CATÁLOGO DE PRODUCTOS ===");
                    System.out.println("ID | Nombre | Categoría | Precio | Stock");
                    System.out.println("----------------------------------------");
                    productCatalogUseCase.getProductList().forEach(p -> 
                        System.out.println(p.getId() + " | " + p.getName() + " | " + 
                                          p.getCategory() + " | $" + p.getPrice() + " | " + p.getStock())
                    );
                    break;
                    
                case 2:
                    // Añadir producto al carrito
                    System.out.print("Ingrese ID del producto: ");
                    String productId = scanner.nextLine();
                    
                    // Validar que el ID sea válido
                    try {
                        Integer.parseInt(productId);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: ID de producto inválido.");
                        break;
                    }
                    
                    System.out.print("Ingrese cantidad: ");
                    int quantity;
                    try {
                        quantity = scanner.nextInt();
                        scanner.nextLine(); // Consumir salto de línea
                        
                        // Validar que la cantidad sea mayor a 0
                        if (quantity <= 0) {
                            System.out.println("Error: La cantidad debe ser mayor a 0.");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error: Cantidad inválida.");
                        scanner.nextLine(); // Limpiar buffer
                        break;
                    }
                    
                    boolean added = shoppingCartUseCase.addProductToCart(productId, quantity);
                    if (added) {
                        System.out.println("Producto añadido al carrito correctamente.");
                    } else {
                        System.out.println("Error: No se pudo añadir el producto. Verifique ID y stock disponible.");
                    }
                    break;
                    
                case 3:
                    // Ver carrito de compras
                    System.out.println("\n=== CARRITO DE COMPRAS ===");
                    if (shoppingCartUseCase.viewCart().getItems().isEmpty()) {
                        System.out.println("El carrito está vacío.");
                    } else {
                        System.out.println("Producto | Cantidad | Precio Unitario | Subtotal");
                        System.out.println("------------------------------------------------");
                        shoppingCartUseCase.viewCart().getItems().forEach(item -> 
                            System.out.println(item.getProduct().getName() + " | " + 
                                              item.getQuantity() + " | $" + 
                                              item.getProduct().getPrice() + " | $" + 
                                              item.getSubtotal())
                        );
                        System.out.println("------------------------------------------------");
                        System.out.println("Total: $" + shoppingCartUseCase.viewCart().getTotalAmount());
                    }
                    break;
                    
                case 4:
                    // Actualizar cantidad de un producto
                    System.out.print("Ingrese ID del producto: ");
                    String updateId = scanner.nextLine();
                    
                    // Validar que el ID sea válido
                    try {
                        Integer.parseInt(updateId);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: ID de producto inválido.");
                        break;
                    }
                    
                    System.out.print("Ingrese nueva cantidad: ");
                    int newQuantity;
                    try {
                        newQuantity = scanner.nextInt();
                        scanner.nextLine(); // Consumir salto de línea
                        
                        // Si la cantidad es 0, preguntar si desea eliminar
                        if (newQuantity == 0) {
                            System.out.print("¿Desea eliminar el producto del carrito? (s/n): ");
                            String respuesta = scanner.nextLine();
                            if (respuesta.equalsIgnoreCase("s")) {
                                boolean removed = shoppingCartUseCase.removeItem(updateId);
                                if (removed) {
                                    System.out.println("Producto eliminado del carrito.");
                                } else {
                                    System.out.println("Error: No se pudo eliminar el producto.");
                                }
                            }
                            break;
                        }
                        
                        // Validar que la cantidad sea mayor a 0
                        if (newQuantity < 0) {
                            System.out.println("Error: La cantidad debe ser mayor o igual a 0.");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error: Cantidad inválida.");
                        scanner.nextLine(); // Limpiar buffer
                        break;
                    }
                    
                    boolean updated = shoppingCartUseCase.updateQuantity(updateId, newQuantity);
                    if (updated) {
                        System.out.println("Cantidad actualizada correctamente.");
                    } else {
                        System.out.println("Error: No se pudo actualizar la cantidad. Verifique ID y stock disponible.");
                    }
                    break;
                    
                case 5:
                    // Eliminar producto del carrito
                    System.out.print("Ingrese ID del producto a eliminar: ");
                    String removeId = scanner.nextLine();
                    
                    boolean removed = shoppingCartUseCase.removeItem(removeId);
                    if (removed) {
                        System.out.println("Producto eliminado del carrito.");
                    } else {
                        System.out.println("Error: No se pudo eliminar el producto. Verifique que exista en el carrito.");
                    }
                    break;
                    
                case 6:
                    // Cerrar pedido (checkout)
                    if (shoppingCartUseCase.viewCart().getItems().isEmpty()) {
                        System.out.println("Error: No se puede cerrar un pedido con el carrito vacío.");
                        break;
                    }
                    
                    Order newOrder = orderUseCase.createOrder();
                    if (newOrder != null) {
                        currentOrderId = newOrder.getOrderId();
                        orderClosed = true;
                        
                        System.out.println("\n=== RESUMEN DEL PEDIDO ===");
                        System.out.println("ID de Pedido: " + newOrder.getOrderId());
                        System.out.println("Fecha: " + newOrder.getDate());
                        System.out.println("\nProductos:");
                        System.out.println("ID | Nombre | Cantidad | Precio Unitario | Subtotal");
                        System.out.println("----------------------------------------------------");
                        
                        newOrder.getItems().forEach(item -> 
                            System.out.println(item.getProduct().getId() + " | " + 
                                             item.getProduct().getName() + " | " + 
                                             item.getQuantity() + " | $" + 
                                             item.getProduct().getPrice() + " | $" + 
                                             item.getSubtotal())
                        );
                        
                        System.out.println("----------------------------------------------------");
                        System.out.println("Subtotal: $" + newOrder.getSubtotal());
                        System.out.println("Descuento: $" + newOrder.getDiscount());
                        System.out.println("Total Final: $" + newOrder.getFinalTotal());
                        
                        // Vaciar el carrito después del cierre
                        orderUseCase.clearCartAfterOrder();
                        System.out.println("\nEl carrito ha sido vaciado. ¡Gracias por su compra!");
                    } else {
                        System.out.println("Error: No se pudo crear la orden.");
                    }
                    break;
                    
                case 7:
                    // Cancelar pedido (vaciar carrito)
                    if (orderClosed) {
                        System.out.println("Error: No es posible cancelar un pedido que ya fue cerrado.");
                    } else {
                        boolean cleared = shoppingCartUseCase.clearCart();
                        if (cleared) {
                            System.out.println("Pedido cancelado. El carrito ha sido vaciado.");
                        } else {
                            System.out.println("Error: No se pudo vaciar el carrito.");
                        }
                    }
                    break;
                    
                case 8:
                    // Ver detalles de la orden
                    if (currentOrderId != null) {
                        Order order = orderUseCase.getOrder(currentOrderId);
                        if (order != null) {
                            System.out.println("\n=== DETALLES DE LA ORDEN ===");
                            System.out.println("ID de Pedido: " + order.getOrderId());
                            System.out.println("Fecha: " + order.getDate());
                            System.out.println("\nProductos:");
                            System.out.println("Nombre | Cantidad | Precio Unitario | Subtotal");
                            System.out.println("--------------------------------------------");
                            
                            order.getItems().forEach(item -> 
                                System.out.println(item.getProduct().getName() + " | " + 
                                                 item.getQuantity() + " | $" + 
                                                 item.getProduct().getPrice() + " | $" + 
                                                 item.getSubtotal())
                            );
                            
                            System.out.println("--------------------------------------------");
                            System.out.println("Subtotal: $" + order.getSubtotal());
                            System.out.println("Descuento: $" + order.getDiscount());
                            System.out.println("Total Final: $" + order.getFinalTotal());
                        } else {
                            System.out.println("Error: No se encontró la orden.");
                        }
                    } else {
                        System.out.println("No hay orden creada aún.");
                    }
                    break;
                    
                case 9:
                    // Salir
                    activo = false;
                    System.out.println("¡Gracias por usar nuestro carrito de compras!");
                    break;
                    
                default:
                    System.out.println("Opción no válida.");
            }
        }
        
        scanner.close();
    }
}