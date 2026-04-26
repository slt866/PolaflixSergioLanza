package es.unican.sergio.polaflix.controller;

import es.unican.sergio.polaflix.dto.SerieDTO;
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
}