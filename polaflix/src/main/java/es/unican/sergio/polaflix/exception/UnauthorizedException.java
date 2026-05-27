package es.unican.sergio.polaflix.exception;

/**
 * Se lanza cuando el usuario intenta realizar una operación no permitida.
 */
public class UnauthorizedException extends PolaflixException {
    
    public UnauthorizedException(String message) {
        super("UNAUTHORIZED", message, 403);
    }
}
