package co.com.poo.shoppingcart.dto.response;

import java.time.LocalDateTime;

/**
 * DTO para devolver errores de forma estandarizada al cliente.
 */
public class ErrorResponseDTO {

    private String code;
    private String message;
    private LocalDateTime timestamp;

    // Constructores
    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
