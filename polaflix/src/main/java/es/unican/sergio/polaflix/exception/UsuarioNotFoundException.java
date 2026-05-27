package es.unican.sergio.polaflix.exception;

/**
 * Se lanza cuando un usuario solicitado no existe en la base de datos.
 */
public class UsuarioNotFoundException extends PolaflixException {
    
    public UsuarioNotFoundException(Long usuarioId) {
        super("USER_NOT_FOUND", "Usuario con ID " + usuarioId + " no encontrado", 404);
    }
}
