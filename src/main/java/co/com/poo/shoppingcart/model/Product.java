package co.com.poo.shoppingcart.model;

public class Product {

    private final Integer id;
    private final String name;
    private final String category;
    private final Double price;
    private Integer stock; // Nuevo atributo para manejar el stock

    public Product(Integer id, String name, String category, Double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = 10; // Valor por defecto para stock inicial
    }
    
    /**
     * Constructor completo que incluye el stock inicial
     */
    public Product(Integer id, String name, String category, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }
    
    /**
     * Obtiene el stock disponible del producto
     * @return cantidad disponible en stock
     */
    public Integer getStock() {
        return stock;
    }
    
    /**
     * Actualiza el stock disponible del producto
     * @param stock nueva cantidad en stock
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
