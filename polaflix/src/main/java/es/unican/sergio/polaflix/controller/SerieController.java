package es.unican.sergio.polaflix.controller;

import es.unican.sergio.polaflix.dto.ApiResponse;
import es.unican.sergio.polaflix.dto.CapituloDTO;
import es.unican.sergio.polaflix.dto.SerieDTO;
import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.exception.SerieNotFoundException;
import es.unican.sergio.polaflix.model.TipoSerie;
import es.unican.sergio.polaflix.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de series de catálogo.
 * 
 * NOTA: Este controlador solo proporciona endpoints para LEER información del catálogo.
 * 
 * Los endpoints para crear, actualizar y eliminar series (administración del catálogo)
 * NO están disponibles aquí. Se asume que existirá un servicio separado de BACKOFFICE
 * para la administración del catálogo de series, gestionado por administradores.
 * 
 * Los usuarios solo pueden consultar series disponibles y gestionar su lista personal de series.
 */
@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    /**
     * GET /series
     * Obtiene el catálogo completo de series disponibles.
     * 
     * @return ApiResponse con lista de todas las series (200 OK)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getAllSeries() {
        List<SerieDTO> series = serieService.findAll();
        return ResponseEntity.ok(ApiResponse.success(series, "Series obtenidas exitosamente"));
    }

    /**
     * GET /series/{id}
     * Obtiene los detalles completos de una serie específica.
     * 
     * @param id ID de la serie
     * @return ApiResponse con datos de la serie (200 OK)
     * @throws SerieNotFoundException si la serie no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SerieDTO>> getSerieById(@PathVariable Long id) {
        SerieDTO serie = serieService.findById(id)
                .orElseThrow(() -> new SerieNotFoundException(id));
        return ResponseEntity.ok(ApiResponse.success(serie, "Serie obtenida exitosamente"));
    }

    /**
     * GET /series/buscar/tipo
     * Busca series por tipo (COMEDIA, DRAMA, etc.).
     * 
     * @param tipo tipo de serie a buscar
     * @return ApiResponse con lista de series del tipo especificado (200 OK)
     */
    @GetMapping("/buscar/tipo")
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getSeriesByTipo(@RequestParam TipoSerie tipo) {
        List<SerieDTO> series = serieService.findByTipo(tipo);
        return ResponseEntity.ok(ApiResponse.success(series, "Series por tipo obtenidas exitosamente"));
    }

    /**
     * GET /series/buscar/titulo
     * Busca series por título (búsqueda parcial).
     * 
     * @param titulo título parcial o completo a buscar
     * @return ApiResponse con lista de series que coinciden (200 OK)
     */
    @GetMapping("/buscar/titulo")
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getSeriesByTitulo(@RequestParam String titulo) {
        List<SerieDTO> series = serieService.findByTitulo(titulo);
        return ResponseEntity.ok(ApiResponse.success(series, "Series por título obtenidas exitosamente"));
    }

    // ==================== ENDPOINTS PARA TEMPORADAS (SOLO LECTURA) ====================

    /**
     * GET /series/{serieId}/temporadas
     * Obtiene todas las temporadas de una serie.
     * 
     * @param serieId ID de la serie
     * @return ApiResponse con lista de temporadas (200 OK)
     * @throws SerieNotFoundException si la serie no existe
     */
    @GetMapping("/{serieId}/temporadas")
    public ResponseEntity<ApiResponse<List<TemporadaDTO>>> getTemporadasDeSerie(@PathVariable Long serieId) {
        List<TemporadaDTO> temporadas = serieService.getTemporadasDeSerie(serieId)
                .orElseThrow(() -> new SerieNotFoundException(serieId));
        return ResponseEntity.ok(ApiResponse.success(temporadas, "Temporadas obtenidas exitosamente"));
    }

    /**
     * GET /series/{serieId}/temporadas/{temporadaId}
     * Obtiene una temporada específica de una serie.
     * 
     * @param serieId ID de la serie
     * @param temporadaId ID de la temporada
     * @return ApiResponse con datos de la temporada (200 OK)
     * @throws SerieNotFoundException si la serie no existe
     */
    @GetMapping("/{serieId}/temporadas/{temporadaId}")
    public ResponseEntity<ApiResponse<TemporadaDTO>> getTemporadaDeSerie(@PathVariable Long serieId, 
                                                                          @PathVariable Long temporadaId) {
        TemporadaDTO temporada = serieService.getTemporadaDeSerie(serieId, temporadaId)
                .orElseThrow(() -> new SerieNotFoundException(serieId));
        return ResponseEntity.ok(ApiResponse.success(temporada, "Temporada obtenida exitosamente"));
    }

    // ==================== ENDPOINTS PARA CAPÍTULOS (SOLO LECTURA) ====================

    /**
     * GET /series/{serieId}/temporadas/{temporadaId}/capitulos
     * Obtiene todos los capítulos de una temporada.
     * 
     * @param serieId ID de la serie
     * @param temporadaId ID de la temporada
     * @return ApiResponse con lista de capítulos (200 OK)
     * @throws SerieNotFoundException si la serie no existe
     */
    @GetMapping("/{serieId}/temporadas/{temporadaId}/capitulos")
    public ResponseEntity<ApiResponse<List<CapituloDTO>>> getCapitulosDeTemporada(@PathVariable Long serieId, 
                                                                                    @PathVariable Long temporadaId) {
        List<CapituloDTO> capitulos = serieService.getCapitulosDeTemporada(serieId, temporadaId)
                .orElseThrow(() -> new SerieNotFoundException(serieId));
        return ResponseEntity.ok(ApiResponse.success(capitulos, "Capítulos obtenidos exitosamente"));
    }

    /**
     * GET /series/{serieId}/temporadas/{temporadaId}/capitulos/{capituloId}
     * Obtiene un capítulo específico de una temporada.
     * 
     * @param serieId ID de la serie
     * @param temporadaId ID de la temporada
     * @param capituloId ID del capítulo
     * @return ApiResponse con datos del capítulo (200 OK)
     * @throws SerieNotFoundException si la serie no existe
     */
    @GetMapping("/{serieId}/temporadas/{temporadaId}/capitulos/{capituloId}")
    public ResponseEntity<ApiResponse<CapituloDTO>> getCapituloDeTemporada(@PathVariable Long serieId, 
                                                                             @PathVariable Long temporadaId, 
                                                                             @PathVariable Long capituloId) {
        CapituloDTO capitulo = serieService.getCapituloDeTemporada(serieId, temporadaId, capituloId)
                .orElseThrow(() -> new SerieNotFoundException(serieId));
        return ResponseEntity.ok(ApiResponse.success(capitulo, "Capítulo obtenido exitosamente"));
    }

    // ==================== NOTA SOBRE ENDPOINTS DE ADMINISTRACIÓN ====================
    // 
    // Los siguientes endpoints NO están disponibles en este controlador:
    // - POST /series (crear serie)
    // - PUT /series/{id} (actualizar serie)
    // - PATCH /series/{id} (actualizar parcialmente)
    // - DELETE /series/{id} (eliminar serie)
    // - POST /{serieId}/temporadas
    // - PUT /{serieId}/temporadas/{temporadaId}
    // - DELETE /{serieId}/temporadas/{temporadaId}
    // - POST /{serieId}/temporadas/{temporadaId}/capitulos
    // - PUT /{serieId}/temporadas/{temporadaId}/capitulos/{capituloId}
    // - DELETE /{serieId}/temporadas/{temporadaId}/capitulos/{capituloId}
    //
    // Estos endpoints son funcionalidad de BACKOFFICE (administración) y deben
    // estar disponibles en un servicio separado de administración, no en la API
    // pública para usuarios.
}