package co.com.poo.shoppingcart.exception;

import co.com.poo.shoppingcart.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 * Intercepta errores y devuelve respuestas JSON estandarizadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Maneja excepciones de producto no encontrado.
   * Retorna HTTP 404 (Not Found)
   */
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleProductNotFoundException(ProductNotFoundException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(
            "PRODUCT_NOT_FOUND",
            ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Maneja excepciones de cantidad inválida.
   * Retorna HTTP 400 (Bad Request)
   */
  @ExceptionHandler(InvalidQuantityException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidQuantityException(InvalidQuantityException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(
            "INVALID_QUANTITY",
            ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Maneja excepciones de carrito vacío.
   * Retorna HTTP 400 (Bad Request)
   */
  @ExceptionHandler(EmptyCartException.class)
  public ResponseEntity<ErrorResponseDTO> handleEmptyCartException(EmptyCartException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(
            "EMPTY_CART",
            ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Maneja errores de validación de DTOs (anotaciones @Valid).
   * Retorna HTTP 400 (Bad Request) con detalles de los campos inválidos.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  /**
   * Maneja cualquier otra excepción no prevista.
   * Retorna HTTP 500 (Internal Server Error)
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(
            "INTERNAL_SERVER_ERROR",
            "Ocurrió un error inesperado. Por favor contacte al administrador."
    );

    // Log del error real (para debugging del desarrollador)
    System.err.println("Error no manejado: " + ex.getMessage());
    ex.printStackTrace();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  /**
   * Maneja excepciones de tipo RuntimeException genéricas.
   * Retorna HTTP 500 (Internal Server Error)
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(
            "RUNTIME_ERROR",
            ex.getMessage() != null ? ex.getMessage() : "Error en tiempo de ejecución"
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
