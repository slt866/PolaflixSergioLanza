package es.unican.sergio.polaflix.exception;

/**
 * Se lanza cuando hay un error de validación en los datos de entrada.
 */
public class ValidationException extends PolaflixException {
    
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message, 400);
    }
}
