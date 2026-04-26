package es.unican.sergio.polaflix.controller;

import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.service.TemporadaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/temporadas")
public class TemporadaController {

    @Autowired
    private TemporadaService temporadaService;

    @GetMapping
    public ResponseEntity<List<TemporadaDTO>> getAllTemporadas() {
        List<TemporadaDTO> temporadas = temporadaService.findAll();
        return ResponseEntity.ok(temporadas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemporadaDTO> getTemporadaById(@PathVariable Long id) {
        return temporadaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TemporadaDTO> createTemporada(@Valid @RequestBody TemporadaDTO temporadaDTO) {
        TemporadaDTO saved = temporadaService.save(temporadaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemporadaDTO> updateTemporada(@PathVariable Long id, @Valid @RequestBody TemporadaDTO temporadaDTO) {
        if (!temporadaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        temporadaDTO.setIdTemp(id);
        TemporadaDTO updated = temporadaService.save(temporadaDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TemporadaDTO> patchTemporada(@PathVariable Long id, @Valid @RequestBody TemporadaDTO temporadaDTO) {
        return temporadaService.patch(id, temporadaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemporada(@PathVariable Long id) {
        if (temporadaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
