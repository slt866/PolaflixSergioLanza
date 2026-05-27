package es.unican.sergio.polaflix.controller;

import es.unican.sergio.polaflix.dto.ApiResponse;
import es.unican.sergio.polaflix.dto.FacturaDTO;
import es.unican.sergio.polaflix.exception.ResourceNotFoundException;
import es.unican.sergio.polaflix.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la consulta de facturas.
 * 
 * NOTA: Este controlador solo proporciona endpoints para LEER información de facturas.
 * 
 * Los endpoints para crear, actualizar y eliminar facturas NO están disponibles.
 * Las facturas se generan automáticamente por el sistema como resultado de la suscripción
 * del usuario y sus visualizaciones. No pueden ser creadas o modificadas manualmente.
 * 
 * Para gestionar facturas (crear, actualizar, eliminar), se asume que existirá
 * un servicio separado de BACKOFFICE para administración.
 */
@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    /**
     * GET /facturas
     * Obtiene el listado de todas las facturas (funcionalidad de administración).
     * 
     * @return ApiResponse con lista de todas las facturas (200 OK)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FacturaDTO>>> getAllFacturas() {
        List<FacturaDTO> facturas = facturaService.findAll();
        return ResponseEntity.ok(ApiResponse.success(facturas, "Facturas obtenidas exitosamente"));
    }

    /**
     * GET /facturas/{id}
     * Obtiene los detalles de una factura específica.
     * 
     * @param id ID de la factura
     * @return ApiResponse con datos de la factura (200 OK)
     * @throws ResourceNotFoundException si la factura no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaDTO>> getFacturaById(@PathVariable Long id) {
        FacturaDTO factura = facturaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
        return ResponseEntity.ok(ApiResponse.success(factura, "Factura obtenida exitosamente"));
    }

    // ==================== NOTA SOBRE ENDPOINTS DE MODIFICACIÓN ====================
    //
    // Los siguientes endpoints NO están disponibles:
    // - POST /facturas (crear factura)
    // - PUT /facturas/{id} (actualizar factura)
    // - PATCH /facturas/{id} (actualizar parcialmente)
    // - DELETE /facturas/{id} (eliminar factura)
    //
    // Las facturas se generan AUTOMÁTICAMENTE por el sistema como resultado de:
    // 1. La suscripción del usuario
    // 2. Las visualizaciones de capítulos realizadas por el usuario
    //
    // No pueden ser creadas, modificadas o eliminadas de forma manual.
    //
    // Para operaciones administrativas de facturas, se asume que existirá
    // un servicio separado de BACKOFFICE.
}
