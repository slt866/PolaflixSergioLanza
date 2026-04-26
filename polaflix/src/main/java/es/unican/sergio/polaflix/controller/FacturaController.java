package es.unican.sergio.polaflix.controller;

import es.unican.sergio.polaflix.dto.FacturaDTO;
import es.unican.sergio.polaflix.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> getAllFacturas() {
        List<FacturaDTO> facturas = facturaService.findAll();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> getFacturaById(@PathVariable Long id) {
        return facturaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FacturaDTO> createFactura(@Valid @RequestBody FacturaDTO facturaDTO) {
        FacturaDTO saved = facturaService.save(facturaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> updateFactura(@PathVariable Long id, @Valid @RequestBody FacturaDTO facturaDTO) {
        if (!facturaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        facturaDTO.setIdFactura(id);
        FacturaDTO updated = facturaService.save(facturaDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FacturaDTO> patchFactura(@PathVariable Long id, @Valid @RequestBody FacturaDTO facturaDTO) {
        return facturaService.patch(id, facturaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Long id) {
        if (facturaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}