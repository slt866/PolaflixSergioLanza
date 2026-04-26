package es.unican.sergio.polaflix.service;

import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.dto.CapituloDTO;
import es.unican.sergio.polaflix.model.Temporada;
import es.unican.sergio.polaflix.model.Capitulo;
import es.unican.sergio.polaflix.repository.TemporadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TemporadaService {

    @Autowired
    private TemporadaRepository temporadaRepository;

    public List<TemporadaDTO> findAll() {
        return temporadaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TemporadaDTO> findById(Long id) {
        return temporadaRepository.findById(id)
                .map(this::convertToDTO);
    }

    public TemporadaDTO save(TemporadaDTO temporadaDTO) {
        Temporada temporada = convertToEntity(temporadaDTO);
        Temporada saved = temporadaRepository.save(temporada);
        return convertToDTO(saved);
    }

    public Optional<TemporadaDTO> patch(Long id, TemporadaDTO temporadaDTO) {
        return temporadaRepository.findById(id)
                .map(temporada -> {
                    if (temporadaDTO.getNumeroTemp() != null) {
                        temporada.setNumeroTemp(temporadaDTO.getNumeroTemp());
                    }
                    if (temporadaDTO.getTitulo() != null) {
                        temporada.setTitulo(temporadaDTO.getTitulo());
                    }
                    Temporada saved = temporadaRepository.save(temporada);
                    return convertToDTO(saved);
                });
    }

    public boolean deleteById(Long id) {
        if (temporadaRepository.existsById(id)) {
            temporadaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TemporadaDTO convertToDTO(Temporada temporada) {
        TemporadaDTO dto = new TemporadaDTO();
        dto.setIdTemp(temporada.getIdTemp());
        dto.setNumeroTemp(temporada.getNumeroTemp());
        dto.setTitulo(temporada.getTitulo());
        
        if (temporada.getCapitulos() != null) {
            List<CapituloDTO> capitulosDTO = temporada.getCapitulos().stream()
                    .map(this::convertCapituloToDTO)
                    .collect(Collectors.toList());
            dto.setCapitulos(capitulosDTO);
            dto.setNumeroCapitulos(capitulosDTO.size());
        }
        
        return dto;
    }

    private Temporada convertToEntity(TemporadaDTO dto) {
        Temporada temporada = new Temporada();
        temporada.setIdTemp(dto.getIdTemp());
        temporada.setNumeroTemp(dto.getNumeroTemp());
        temporada.setTitulo(dto.getTitulo());
        // Note: Capitulos would need to be set separately
        return temporada;
    }

    private CapituloDTO convertCapituloToDTO(Capitulo capitulo) {
        CapituloDTO dto = new CapituloDTO();
        dto.setIdCap(capitulo.getIdCap());
        dto.setNumeroCap(capitulo.getNumeroCap());
        dto.setTitulo(capitulo.getTitulo());
        dto.setDescripcion(capitulo.getDescripcion());
        return dto;
    }
}
