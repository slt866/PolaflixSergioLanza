package es.unican.sergio.polaflix.exception;

/**
 * Se lanza cuando un recurso solicitado no existe.
 */
public class ResourceNotFoundException extends PolaflixException {
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("RESOURCE_NOT_FOUND", resourceName + " no encontrado con " + fieldName + ": " + fieldValue, 404);
    }
}
