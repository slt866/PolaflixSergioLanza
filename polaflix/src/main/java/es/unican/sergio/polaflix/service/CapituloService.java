package es.unican.sergio.polaflix.service;

import es.unican.sergio.polaflix.dto.CapituloDTO;
import es.unican.sergio.polaflix.model.Capitulo;
import es.unican.sergio.polaflix.repository.CapituloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CapituloService {

    @Autowired
    private CapituloRepository capituloRepository;

    public List<CapituloDTO> findAll() {
        return capituloRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CapituloDTO> findById(Long id) {
        return capituloRepository.findById(id)
                .map(this::convertToDTO);
    }

    public CapituloDTO save(CapituloDTO capituloDTO) {
        Capitulo capitulo = convertToEntity(capituloDTO);
        Capitulo saved = capituloRepository.save(capitulo);
        return convertToDTO(saved);
    }

    public Optional<CapituloDTO> patch(Long id, CapituloDTO capituloDTO) {
        return capituloRepository.findById(id)
                .map(capitulo -> {
                    if (capituloDTO.getNumeroCap() != null) {
                        capitulo.setNumeroCap(capituloDTO.getNumeroCap());
                    }
                    if (capituloDTO.getTitulo() != null) {
                        capitulo.setTitulo(capituloDTO.getTitulo());
                    }
                    if (capituloDTO.getDescripcion() != null) {
                        capitulo.setDescripcion(capituloDTO.getDescripcion());
                    }
                    Capitulo saved = capituloRepository.save(capitulo);
                    return convertToDTO(saved);
                });
    }

    public boolean deleteById(Long id) {
        if (capituloRepository.existsById(id)) {
            capituloRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private CapituloDTO convertToDTO(Capitulo capitulo) {
        CapituloDTO dto = new CapituloDTO();
        dto.setIdCap(capitulo.getIdCap());
        dto.setNumeroCap(capitulo.getNumeroCap());
        dto.setTitulo(capitulo.getTitulo());
        dto.setDescripcion(capitulo.getDescripcion());
        return dto;
    }

    private Capitulo convertToEntity(CapituloDTO dto) {
        Capitulo capitulo = new Capitulo();
        capitulo.setIdCap(dto.getIdCap());
        capitulo.setNumeroCap(dto.getNumeroCap());
        capitulo.setTitulo(dto.getTitulo());
        capitulo.setDescripcion(dto.getDescripcion());
        // Note: Temporada would need to be set separately
        return capitulo;
    }
}
