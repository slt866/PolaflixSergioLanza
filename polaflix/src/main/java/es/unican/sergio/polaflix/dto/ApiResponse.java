package es.unican.sergio.polaflix.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Envoltorio genérico para todas las respuestas de la API.
 * Proporciona un modelo consistente para devolver datos, errores y metadatos.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    /**
     * Código HTTP de la respuesta
     */
    private Integer status;
    
    /**
     * Código de error específico del negocio (ej: USER_NOT_FOUND, INVALID_SUBSCRIPTION)
     */
    private String errorCode;
    
    /**
     * Mensaje descriptivo para el cliente
     */
    private String message;
    
    /**
     * Datos devueltos en la respuesta
     */
    private T data;
    
    /**
     * Timestamp de la respuesta
     */
    private LocalDateTime timestamp;
    
    /**
     * Constructor para respuestas exitosas con datos
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(200)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Constructor para respuestas exitosas de creación
     */
    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .status(201)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Constructor para respuestas de error
     */
    public static <T> ApiResponse<T> error(Integer status, String errorCode, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .errorCode(errorCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Constructor para respuesta de No Content (204)
     */
    public static <Void> ApiResponse<Void> noContent() {
        return ApiResponse.<Void>builder()
                .status(204)
                .message("Recurso eliminado exitosamente")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
