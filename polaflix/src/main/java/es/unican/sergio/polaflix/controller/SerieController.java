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
}