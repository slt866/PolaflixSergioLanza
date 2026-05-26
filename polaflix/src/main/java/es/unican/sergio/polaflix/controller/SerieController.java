package es.unican.sergio.polaflix.controller;

import es.unican.sergio.polaflix.dto.SerieDTO;
import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.dto.CapituloDTO;
import es.unican.sergio.polaflix.model.TipoSerie;
import es.unican.sergio.polaflix.service.SerieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping
    public ResponseEntity<List<SerieDTO>> getAllSeries() {
        List<SerieDTO> series = serieService.findAll();
        return ResponseEntity.ok(series);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SerieDTO> getSerieById(@PathVariable Long id) {
        return serieService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SerieDTO> createSerie(@Valid @RequestBody SerieDTO serieDTO) {
        SerieDTO saved = serieService.save(serieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SerieDTO> updateSerie(@PathVariable Long id, @Valid @RequestBody SerieDTO serieDTO) {
        if (!serieService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        serieDTO.setIdSerie(id);
        SerieDTO updated = serieService.save(serieDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SerieDTO> patchSerie(@PathVariable Long id, @Valid @RequestBody SerieDTO serieDTO) {
        return serieService.patch(id, serieDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSerie(@PathVariable Long id) {
        if (serieService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar/tipo")
    public ResponseEntity<List<SerieDTO>> getSeriesByTipo(@RequestParam TipoSerie tipo) {
        List<SerieDTO> series = serieService.findByTipo(tipo);
        return ResponseEntity.ok(series);
    }

    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<SerieDTO>> getSeriesByTitulo(@RequestParam String titulo) {
        List<SerieDTO> series = serieService.findByTitulo(titulo);
        return ResponseEntity.ok(series);
    }

    // ==================== ENDPOINTS PARA TEMPORADAS ====================

    @GetMapping("/{serieId}/temporadas")
    public ResponseEntity<List<TemporadaDTO>> getTemporadasDeSerie(@PathVariable Long serieId) {
        return serieService.getTemporadasDeSerie(serieId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{serieId}/temporadas/{temporadaId}")
    public ResponseEntity<TemporadaDTO> getTemporadaDeSerie(@PathVariable Long serieId, @PathVariable Long temporadaId) {
        return serieService.getTemporadaDeSerie(serieId, temporadaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{serieId}/temporadas")
    public ResponseEntity<TemporadaDTO> createTemporadaEnSerie(@PathVariable Long serieId, @Valid @RequestBody TemporadaDTO temporadaDTO) {
        return serieService.createTemporadaEnSerie(serieId, temporadaDTO)
                .map(temporada -> ResponseEntity.status(HttpStatus.CREATED).body(temporada))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{serieId}/temporadas/{temporadaId}")
    public ResponseEntity<TemporadaDTO> updateTemporadaDeSerie(@PathVariable Long serieId, @PathVariable Long temporadaId, @Valid @RequestBody TemporadaDTO temporadaDTO) {
        return serieService.updateTemporadaDeSerie(serieId, temporadaId, temporadaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{serieId}/temporadas/{temporadaId}")
    public ResponseEntity<Void> deleteTemporadaDeSerie(@PathVariable Long serieId, @PathVariable Long temporadaId) {
        if (serieService.deleteTemporadaDeSerie(serieId, temporadaId).orElse(false)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== ENDPOINTS PARA CAPÍTULOS ====================

    @GetMapping("/{serieId}/temporadas/{temporadaId}/capitulos")
    public ResponseEntity<List<CapituloDTO>> getCapitulosDeTemporada(@PathVariable Long serieId, @PathVariable Long temporadaId) {
        return serieService.getCapitulosDeTemporada(serieId, temporadaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{serieId}/temporadas/{temporadaId}/capitulos/{capituloId}")
    public ResponseEntity<CapituloDTO> getCapituloDeTemporada(@PathVariable Long serieId, @PathVariable Long temporadaId, @PathVariable Long capituloId) {
        return serieService.getCapituloDeTemporada(serieId, temporadaId, capituloId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{serieId}/temporadas/{temporadaId}/capitulos")
    public ResponseEntity<CapituloDTO> createCapituloEnTemporada(@PathVariable Long serieId, @PathVariable Long temporadaId, @Valid @RequestBody CapituloDTO capituloDTO) {
        return serieService.createCapituloEnTemporada(serieId, temporadaId, capituloDTO)
                .map(capitulo -> ResponseEntity.status(HttpStatus.CREATED).body(capitulo))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{serieId}/temporadas/{temporadaId}/capitulos/{capituloId}")
    public ResponseEntity<CapituloDTO> updateCapituloDeTemporada(@PathVariable Long serieId, @PathVariable Long temporadaId, @PathVariable Long capituloId, @Valid @RequestBody CapituloDTO capituloDTO) {
        return serieService.updateCapituloDeTemporada(serieId, temporadaId, capituloId, capituloDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{serieId}/temporadas/{temporadaId}/capitulos/{capituloId}")
    public ResponseEntity<Void> deleteCapituloDeTemporada(@PathVariable Long serieId, @PathVariable Long temporadaId, @PathVariable Long capituloId) {
        if (serieService.deleteCapituloDeTemporada(serieId, temporadaId, capituloId).orElse(false)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}