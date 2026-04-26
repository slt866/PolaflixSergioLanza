package es.unican.sergio.polaflix.service;

import es.unican.sergio.polaflix.dto.EntradaFacturaDTO;
import es.unican.sergio.polaflix.dto.FacturaDTO;
import es.unican.sergio.polaflix.model.Factura;
import es.unican.sergio.polaflix.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    public List<FacturaDTO> findAll() {
        return facturaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FacturaDTO> findById(Long id) {
        return facturaRepository.findById(id)
                .map(this::convertToDTO);
    }

    public FacturaDTO save(FacturaDTO facturaDTO) {
        Factura factura = convertToEntity(facturaDTO);
        Factura saved = facturaRepository.save(factura);
        return convertToDTO(saved);
    }

    public Optional<FacturaDTO> patch(Long id, FacturaDTO facturaDTO) {
        return facturaRepository.findById(id)
                .map(factura -> {
                    if (facturaDTO.getImporteTotal() != null) {
                        factura.setImporteTotal(facturaDTO.getImporteTotal());
                    }
                    if (facturaDTO.getMes() != null) {
                        factura.setMes(facturaDTO.getMes());
                    }
                    if (facturaDTO.getAno() != null) {
                        factura.setAno(facturaDTO.getAno());
                    }
                    Factura saved = facturaRepository.save(factura);
                    return convertToDTO(saved);
                });
    }

    public boolean deleteById(Long id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private FacturaDTO convertToDTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setIdFactura(factura.getIdFactura());
        dto.setImporteTotal(factura.getImporteTotal());
        dto.setMes(factura.getMes());
        dto.setAno(factura.getAno());
        if (factura.getEntradas() != null) {
            dto.setEntradas(factura.getEntradas().stream()
                    .map(this::convertEntradaToDTO)
                    .collect(Collectors.toList()));
        }
        if (factura.getUsuario() != null) {
            dto.setUsuarioNombre(factura.getUsuario().getNombreUsuario());
        }
        return dto;
    }

    private Factura convertToEntity(FacturaDTO dto) {
        Factura factura = new Factura();
        factura.setIdFactura(dto.getIdFactura());
        factura.setImporteTotal(dto.getImporteTotal());
        factura.setMes(dto.getMes());
        factura.setAno(dto.getAno());
        // Note: Entradas and Usuario would need to be set separately
        return factura;
    }

    private EntradaFacturaDTO convertEntradaToDTO(es.unican.sergio.polaflix.model.EntradaFactura entrada) {
        EntradaFacturaDTO dto = new EntradaFacturaDTO();
        dto.setIdEntradaFactura(entrada.getIdEntradaFactura());
        dto.setFecha(entrada.getFecha());
        dto.setCargo(entrada.getCargo());
        dto.setNumeroTemp(entrada.getNumeroTemp());
        dto.setNumeroCap(entrada.getNumeroCap());
        // Assuming EntradaFactura has a Serie reference, but from model it doesn't directly
        // For simplicity, set to null or handle accordingly
        dto.setSerieTitulo("Unknown"); // Placeholder
        return dto;
    }
}