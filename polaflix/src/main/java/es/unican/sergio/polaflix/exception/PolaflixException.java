package es.unican.sergio.polaflix.exception;

/**
 * Excepción base para todas las excepciones de negocio de Polaflix.
 * Cada excepción contiene un código de error específico para el cliente.
 */
public abstract class PolaflixException extends RuntimeException {
    
    private final String errorCode;
    private final int httpStatus;
    
    public PolaflixException(String errorCode, String message, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public int getHttpStatus() {
        return httpStatus;
    }
}
