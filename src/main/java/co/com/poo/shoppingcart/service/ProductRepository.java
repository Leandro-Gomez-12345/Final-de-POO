package co.com.poo.shoppingcart.service;

import co.com.poo.shoppingcart.model.Product;
import java.util.List;

/**
 * Representa un repositorio para almacenar y recuperar productos.
 */

public interface ProductRepository {

    /**
     * Carga la lista de productos disponibles.
     * @return Una lista de productos.
     */

    List<Product> loadProducts();

    /**
     * Recupera un producto por su ID.
     * @param id El ID del producto a recuperar.
     * @return El producto con el ID especificado, o null si no se encuentra.
     */

    Product getProductById(Integer id);
}