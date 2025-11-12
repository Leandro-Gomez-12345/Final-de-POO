package co.com.poo.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de Spring Boot.
 * Esta clase arranca toda la aplicación como un api rest.
 */

@SpringBootApplication
public class ShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
        System.out.println("Api rest: Carrito de Compras");
        System.out.println("Servidor corriendo en: http://localhost:8080");
    }

}