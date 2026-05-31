package es.unican.sergio.polaflix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.unican.sergio.polaflix.dto.ApiResponse;
import es.unican.sergio.polaflix.dto.FacturaDTO;
import es.unican.sergio.polaflix.dto.ProgresoSerieDTO;
import es.unican.sergio.polaflix.dto.SerieDTO;
import es.unican.sergio.polaflix.dto.SuscripcionDTO;
import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.dto.UsuarioDTO;
import es.unican.sergio.polaflix.exception.UsuarioNotFoundException;
import es.unican.sergio.polaflix.service.UsuarioService;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para operaciones CRUD de usuarios y gestión de series.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * GET /usuarios/{id}
     * Obtiene un usuario específico por su ID con todos sus detalles (Vista Detallada).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> getUsuarioById(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        return ResponseEntity.ok(ApiResponse.success(usuario, "Usuario obtenido exitosamente"));
    }

    // ==================== GESTIÓN DE SERIES Y PROGRESO ====================

    @PostMapping("/{usuarioId}/series/{serieId}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> addSeriePendiente(@PathVariable Long usuarioId, 
                                                                      @PathVariable Long serieId) {
        UsuarioDTO usuarioDTO = usuarioService.addSeriePendiente(usuarioId, serieId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(usuarioDTO, "Serie agregada a pendientes exitosamente"));
    }

    @GetMapping("/{usuarioId}/series/pendientes")
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getSeriesPendientes(@PathVariable Long usuarioId) {
        List<SerieDTO> series = usuarioService.getSeriesPendientes(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(series, "Series pendientes obtenidas exitosamente"));
    }

    @GetMapping("/{usuarioId}/series/empezadas")
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getSeriesEmpezadas(@PathVariable Long usuarioId) {
        List<SerieDTO> series = usuarioService.getSeriesEmpezadas(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(series, "Series empezadas obtenidas exitosamente"));
    }

    @GetMapping("/{usuarioId}/series/terminadas")
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getSeriesTerminadas(@PathVariable Long usuarioId) {
        List<SerieDTO> series = usuarioService.getSeriesTerminadas(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(series, "Series terminadas obtenidas exitosamente"));
    }

    @PostMapping("/{usuarioId}/capitulos/visto")
    public ResponseEntity<ApiResponse<UsuarioDTO>> marcarCapituloVisto(@PathVariable Long usuarioId, 
                                                                        @RequestParam Long serieId,
                                                                        @RequestParam int temp,
                                                                        @RequestParam int cap) {
        UsuarioDTO usuarioDTO = usuarioService.marcarCapituloVisto(usuarioId, serieId, temp, cap)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(usuarioDTO, "Capítulo marcado como visto exitosamente"));
    }

    @GetMapping("/{usuarioId}/facturas")
    public ResponseEntity<ApiResponse<List<FacturaDTO>>> getFacturasDeUsuario(@PathVariable Long usuarioId) {
        List<FacturaDTO> facturas = usuarioService.getFacturasDeUsuario(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(facturas, "Facturas obtenidas exitosamente"));
    }

    @GetMapping("/{usuarioId}/progreso/{serieId}")
    public ResponseEntity<ApiResponse<ProgresoSerieDTO>> getProgresoSerie(@PathVariable Long usuarioId, 
                                                                           @PathVariable Long serieId) {
        ProgresoSerieDTO progreso = usuarioService.getProgresoSerie(usuarioId, serieId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(progreso, "Progreso obtenido exitosamente"));
    }

    /*@GetMapping("/{usuarioId}/series/{serieId}/terminada")
    public ResponseEntity<ApiResponse<Boolean>> esSerieTerminada(@PathVariable Long usuarioId, 
                                                                  @PathVariable Long serieId) {
        Boolean esTerminada = usuarioService.esSerieTerminadaPorUsuario(usuarioId, serieId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(esTerminada, "Estado de serie obtenido exitosamente"));
    }*/
}