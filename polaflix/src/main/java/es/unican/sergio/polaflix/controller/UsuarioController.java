package es.unican.sergio.polaflix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import es.unican.sergio.polaflix.dto.UsuarioCreateDTO;
import es.unican.sergio.polaflix.dto.UsuarioDTO;
import es.unican.sergio.polaflix.dto.UsuarioUpdateDTO;
import es.unican.sergio.polaflix.exception.UsuarioNotFoundException;
import es.unican.sergio.polaflix.service.UsuarioService;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para operaciones CRUD de usuarios y gestión de series.
 * 
 * Endpoints para administración (backoffice) no incluidos aquí.
 * Se asume que existirá un servicio separado para administración de usuarios.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * GET /usuarios
     * Obtiene la lista de todos los usuarios registrados.
     * 
     * @return ApiResponse con lista de usuarios (200 OK)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(ApiResponse.success(usuarios, "Usuarios obtenidos exitosamente"));
    }

    /**
     * GET /usuarios/{id}
     * Obtiene un usuario específico por su ID.
     * 
     * @param id ID del usuario
     * @return ApiResponse con datos del usuario (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> getUsuarioById(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        return ResponseEntity.ok(ApiResponse.success(usuario, "Usuario obtenido exitosamente"));
    }

    /**
     * POST /usuarios
     * Crea un nuevo usuario en el sistema.
     * 
     * @param usuarioCreateDTO datos del nuevo usuario
     * @return ApiResponse con datos del usuario creado (201 CREATED)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioDTO>> createUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        // Convertir CreateDTO a DTO para la lógica de negocio
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombreUsuario(usuarioCreateDTO.getNombreUsuario());
        usuarioDTO.setCuentaBancaria(usuarioCreateDTO.getCuentaBancaria());
        
        UsuarioDTO saved = usuarioService.save(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Usuario creado exitosamente"));
    }

    /**
     * POST /usuarios/{id} - NOTA: Usar PATCH para actualizaciones parciales
     * Esta operación usa UpdateDTO para actualizaciones parciales.
     * 
     * @param id ID del usuario
     * @param usuarioUpdateDTO datos a actualizar
     * @return ApiResponse con usuario actualizado (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> updateUsuario(@PathVariable Long id, 
                                                                   @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        if (!usuarioService.findById(id).isPresent()) {
            throw new UsuarioNotFoundException(id);
        }
        
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        if (usuarioUpdateDTO.getNombreUsuario() != null) {
            usuarioDTO.setNombreUsuario(usuarioUpdateDTO.getNombreUsuario());
        }
        if (usuarioUpdateDTO.getCuentaBancaria() != null) {
            usuarioDTO.setCuentaBancaria(usuarioUpdateDTO.getCuentaBancaria());
        }
        
        UsuarioDTO updated = usuarioService.save(usuarioDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Usuario actualizado exitosamente"));
    }

    /**
     * DELETE /usuarios/{id}
     * Elimina un usuario del sistema.
     * 
     * @param id ID del usuario a eliminar
     * @return ApiResponse (204 NO CONTENT)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUsuario(@PathVariable Long id) {
        if (!usuarioService.deleteById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.noContent());
    }

    /**
     * POST /usuarios/{usuarioId}/series/{serieId}
     * Agrega una serie a la lista de pendientes del usuario.
     * 
     * @param usuarioId ID del usuario
     * @param serieId ID de la serie
     * @return ApiResponse con usuario actualizado (201 CREATED)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @PostMapping("/{usuarioId}/series/{serieId}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> addSeriePendiente(@PathVariable Long usuarioId, 
                                                                      @PathVariable Long serieId) {
        UsuarioDTO usuarioDTO = usuarioService.addSeriePendiente(usuarioId, serieId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(usuarioDTO, "Serie agregada a pendientes exitosamente"));
    }

    /**
     * GET /usuarios/{usuarioId}/series/pendientes
     * Obtiene la lista de series pendientes del usuario.
     * 
     * @param usuarioId ID del usuario
     * @return ApiResponse con lista de series (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{usuarioId}/series/pendientes")
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getSeriesPendientes(@PathVariable Long usuarioId) {
        List<SerieDTO> series = usuarioService.getSeriesPendientes(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(series, "Series pendientes obtenidas exitosamente"));
    }

    /**
     * GET /usuarios/{usuarioId}/series/empezadas
     * Obtiene la lista de series empezadas por el usuario.
     * 
     * @param usuarioId ID del usuario
     * @return ApiResponse con lista de series (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{usuarioId}/series/empezadas")
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getSeriesEmpezadas(@PathVariable Long usuarioId) {
        List<SerieDTO> series = usuarioService.getSeriesEmpezadas(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(series, "Series empezadas obtenidas exitosamente"));
    }

    /**
     * GET /usuarios/{usuarioId}/series/terminadas
     * Obtiene la lista de series completadas por el usuario.
     * 
     * @param usuarioId ID del usuario
     * @return ApiResponse con lista de series (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{usuarioId}/series/terminadas")
    public ResponseEntity<ApiResponse<List<SerieDTO>>> getSeriesTerminadas(@PathVariable Long usuarioId) {
        List<SerieDTO> series = usuarioService.getSeriesTerminadas(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(series, "Series terminadas obtenidas exitosamente"));
    }

    /**
     * POST /usuarios/{usuarioId}/capitulos/visto
     * Marca un capítulo como visto por el usuario.
     * 
     * @param usuarioId ID del usuario
     * @param serieId ID de la serie
     * @param temp número de temporada
     * @param cap número de capítulo
     * @return ApiResponse con usuario actualizado (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @PostMapping("/{usuarioId}/capitulos/visto")
    public ResponseEntity<ApiResponse<UsuarioDTO>> marcarCapituloVisto(@PathVariable Long usuarioId, 
                                                                        @RequestParam Long serieId,
                                                                        @RequestParam int temp,
                                                                        @RequestParam int cap) {
        UsuarioDTO usuarioDTO = usuarioService.marcarCapituloVisto(usuarioId, serieId, temp, cap)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(usuarioDTO, "Capítulo marcado como visto exitosamente"));
    }

    /**
     * GET /usuarios/{usuarioId}/facturas
     * Obtiene el historial de facturas del usuario.
     * 
     * @param usuarioId ID del usuario
     * @return ApiResponse con lista de facturas (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{usuarioId}/facturas")
    public ResponseEntity<ApiResponse<List<FacturaDTO>>> getFacturasDeUsuario(@PathVariable Long usuarioId) {
        List<FacturaDTO> facturas = usuarioService.getFacturasDeUsuario(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(facturas, "Facturas obtenidas exitosamente"));
    }

    /**
     * GET /usuarios/{usuarioId}/suscripcion
     * Obtiene la suscripción actual del usuario.
     * 
     * @param usuarioId ID del usuario
     * @return ApiResponse con datos de suscripción (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{usuarioId}/suscripcion")
    public ResponseEntity<ApiResponse<SuscripcionDTO>> getSuscripcionDeUsuario(@PathVariable Long usuarioId) {
        SuscripcionDTO suscripcion = usuarioService.getSuscripcionDeUsuario(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(suscripcion, "Suscripción obtenida exitosamente"));
    }

    /**
     * GET /usuarios/{usuarioId}/progreso/{serieId}
     * Obtiene el progreso del usuario en una serie específica.
     * 
     * @param usuarioId ID del usuario
     * @param serieId ID de la serie
     * @return ApiResponse con datos de progreso (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{usuarioId}/progreso/{serieId}")
    public ResponseEntity<ApiResponse<ProgresoSerieDTO>> getProgresoSerie(@PathVariable Long usuarioId, 
                                                                           @PathVariable Long serieId) {
        ProgresoSerieDTO progreso = usuarioService.getProgresoSerie(usuarioId, serieId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(progreso, "Progreso obtenido exitosamente"));
    }

    /**
     * GET /usuarios/{usuarioId}/series/{serieId}/ultima-temporada-vista
     * Obtiene la última temporada vista por el usuario de una serie.
     * 
     * @param usuarioId ID del usuario
     * @param serieId ID de la serie
     * @return ApiResponse con datos de temporada (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{usuarioId}/series/{serieId}/ultima-temporada-vista")
    public ResponseEntity<ApiResponse<TemporadaDTO>> getUltimaTempVistaDeSerie(@PathVariable Long usuarioId, 
                                                                                 @PathVariable Long serieId) {
        TemporadaDTO temporada = usuarioService.getUltimaTempVistaDeSerie(usuarioId, serieId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(temporada, "Última temporada vista obtenida exitosamente"));
    }

    /**
     * GET /usuarios/{usuarioId}/series/{serieId}/terminada
     * Verifica si el usuario ha completado una serie.
     * 
     * @param usuarioId ID del usuario
     * @param serieId ID de la serie
     * @return ApiResponse con boolean indicando si la serie está terminada (200 OK)
     * @throws UsuarioNotFoundException si el usuario no existe
     */
    @GetMapping("/{usuarioId}/series/{serieId}/terminada")
    public ResponseEntity<ApiResponse<Boolean>> esSerieTerminada(@PathVariable Long usuarioId, 
                                                                  @PathVariable Long serieId) {
        Boolean esTerminada = usuarioService.esSerieTerminadaPorUsuario(usuarioId, serieId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        return ResponseEntity.ok(ApiResponse.success(esTerminada, "Estado de serie obtenido exitosamente"));
    }
}