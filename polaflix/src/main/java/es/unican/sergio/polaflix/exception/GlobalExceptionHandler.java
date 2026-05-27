package es.unican.sergio.polaflix.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import es.unican.sergio.polaflix.dto.ApiResponse;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para la API REST.
 * Captura todas las excepciones no manejadas y devuelve una respuesta estandarizada.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    /**
     * Maneja excepciones de validación de argumentos (@Valid)
     * Retorna código 400 (Bad Request) con detalles de los errores de validación
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        ApiResponse<Object> response = ApiResponse.error(
                400,
                "VALIDATION_ERROR",
                "Error en la validación: " + errors
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Maneja excepciones personalizadas de Polaflix
     * Retorna el código de error específico del negocio
     */
    @ExceptionHandler(PolaflixException.class)
    public ResponseEntity<ApiResponse<Object>> handlePolaflixException(PolaflixException ex) {
        ApiResponse<Object> response = ApiResponse.error(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getHttpStatus()));
    }
    
    /**
     * Maneja excepciones de recurso no encontrado (404)
     */
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsuarioNotFoundException(UsuarioNotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Maneja excepciones de serie no encontrada (404)
     */
    @ExceptionHandler(SerieNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleSerieNotFoundException(SerieNotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Maneja cualquier otra excepción no esperada (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.error(
                500,
                "INTERNAL_SERVER_ERROR",
                "Error interno del servidor: " + ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
