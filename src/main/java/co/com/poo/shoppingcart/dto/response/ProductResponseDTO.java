package co.com.poo.shoppingcart.dto.response;

/**
 * DTO para devolver información de un producto al cliente.
 * Solo incluye los campos necesarios (no expone detalles internos).
 */
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;

    // Constructores
    public ProductResponseDTO() {}

    public ProductResponseDTO(Long id, String name, String category, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}