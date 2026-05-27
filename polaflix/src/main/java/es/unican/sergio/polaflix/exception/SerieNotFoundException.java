package es.unican.sergio.polaflix.exception;

/**
 * Se lanza cuando una serie solicitada no existe en la base de datos.
 */
public class SerieNotFoundException extends PolaflixException {
    
    public SerieNotFoundException(Long serieId) {
        super("SERIE_NOT_FOUND", "Serie con ID " + serieId + " no encontrada", 404);
    }
}
