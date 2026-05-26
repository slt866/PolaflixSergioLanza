package es.unican.sergio.polaflix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.unican.sergio.polaflix.dto.FacturaDTO;
import es.unican.sergio.polaflix.dto.ProgresoSerieDTO;
import es.unican.sergio.polaflix.dto.SerieDTO;
import es.unican.sergio.polaflix.dto.SuscripcionDTO;
import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.dto.UsuarioDTO;
import es.unican.sergio.polaflix.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO saved = usuarioService.save(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioDTO.setIdUsuario(id);
        UsuarioDTO updated = usuarioService.save(usuarioDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioDTO> patchUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.patch(id, usuarioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (usuarioService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoints para gestionar series de un usuario
    @PostMapping("/{usuarioId}/series/{serieId}")
    public ResponseEntity<UsuarioDTO> addSeriePendiente(@PathVariable Long usuarioId, @PathVariable Long serieId) {
        return usuarioService.addSeriePendiente(usuarioId, serieId)
                .map(usuarioDTO -> ResponseEntity.status(HttpStatus.CREATED).body(usuarioDTO))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/series/pendientes")
    public ResponseEntity<List<SerieDTO>> getSeriesPendientes(@PathVariable Long usuarioId) {
        return usuarioService.getSeriesPendientes(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/series/empezadas")
    public ResponseEntity<List<SerieDTO>> getSeriesEmpezadas(@PathVariable Long usuarioId) {
        return usuarioService.getSeriesEmpezadas(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/series/terminadas")
    public ResponseEntity<List<SerieDTO>> getSeriesTerminadas(@PathVariable Long usuarioId) {
        return usuarioService.getSeriesTerminadas(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{usuarioId}/capitulos/visto")
    public ResponseEntity<UsuarioDTO> marcarCapituloVisto(@PathVariable Long usuarioId, 
                                                           @RequestParam Long serieId,
                                                           @RequestParam int temp,
                                                           @RequestParam int cap) {
        return usuarioService.marcarCapituloVisto(usuarioId, serieId, temp, cap)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/facturas")
    public ResponseEntity<List<FacturaDTO>> getFacturasDeUsuario(@PathVariable Long usuarioId) {
        return usuarioService.getFacturasDeUsuario(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/suscripcion")
    public ResponseEntity<SuscripcionDTO> getSuscripcionDeUsuario(@PathVariable Long usuarioId) {
        return usuarioService.getSuscripcionDeUsuario(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/progreso/{serieId}")
    public ResponseEntity<ProgresoSerieDTO> getProgresoSerie(@PathVariable Long usuarioId, @PathVariable Long serieId) {
        return usuarioService.getProgresoSerie(usuarioId, serieId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/series/{serieId}/ultima-temporada-vista")
    public ResponseEntity<TemporadaDTO> getUltimaTempVistaDeSerie(@PathVariable Long usuarioId, @PathVariable Long serieId) {
        return usuarioService.getUltimaTempVistaDeSerie(usuarioId, serieId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/series/{serieId}/terminada")
    public ResponseEntity<Boolean> esSerieTerminada(@PathVariable Long usuarioId, @PathVariable Long serieId) {
        return usuarioService.esSerieTerminadaPorUsuario(usuarioId, serieId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}