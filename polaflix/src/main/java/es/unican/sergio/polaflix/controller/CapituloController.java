package es.unican.sergio.polaflix.controller;

import es.unican.sergio.polaflix.dto.CapituloDTO;
import es.unican.sergio.polaflix.service.CapituloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capitulos")
public class CapituloController {

    @Autowired
    private CapituloService capituloService;

    @GetMapping
    public ResponseEntity<List<CapituloDTO>> getAllCapitulos() {
        List<CapituloDTO> capitulos = capituloService.findAll();
        return ResponseEntity.ok(capitulos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapituloDTO> getCapituloById(@PathVariable Long id) {
        return capituloService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CapituloDTO> createCapitulo(@Valid @RequestBody CapituloDTO capituloDTO) {
        CapituloDTO saved = capituloService.save(capituloDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CapituloDTO> updateCapitulo(@PathVariable Long id, @Valid @RequestBody CapituloDTO capituloDTO) {
        if (!capituloService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        capituloDTO.setIdCap(id);
        CapituloDTO updated = capituloService.save(capituloDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CapituloDTO> patchCapitulo(@PathVariable Long id, @Valid @RequestBody CapituloDTO capituloDTO) {
        return capituloService.patch(id, capituloDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCapitulo(@PathVariable Long id) {
        if (capituloService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
