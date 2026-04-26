package es.unican.sergio.polaflix.service;

import es.unican.sergio.polaflix.dto.SerieDTO;
import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.dto.CapituloDTO;
import es.unican.sergio.polaflix.dto.PersonaDTO;
import es.unican.sergio.polaflix.model.Serie;
import es.unican.sergio.polaflix.model.Temporada;
import es.unican.sergio.polaflix.model.Capitulo;
import es.unican.sergio.polaflix.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> findAll() {
        return serieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SerieDTO> findById(Long id) {
        return serieRepository.findById(id)
                .map(this::convertToDTO);
    }

    public SerieDTO save(SerieDTO serieDTO) {
        Serie serie = convertToEntity(serieDTO);
        Serie saved = serieRepository.save(serie);
        return convertToDTO(saved);
    }

    public Optional<SerieDTO> patch(Long id, SerieDTO serieDTO) {
        return serieRepository.findById(id)
                .map(serie -> {
                    if (serieDTO.getTitulo() != null) {
                        serie.setTitulo(serieDTO.getTitulo());
                    }
                    if (serieDTO.getSinopsis() != null) {
                        serie.setSinopsis(serieDTO.getSinopsis());
                    }
                    if (serieDTO.getTipo() != null) {
                        serie.setTipo(serieDTO.getTipo());
                    }
                    Serie saved = serieRepository.save(serie);
                    return convertToDTO(saved);
                });
    }

    public boolean deleteById(Long id) {
        if (serieRepository.existsById(id)) {
            serieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private SerieDTO convertToDTO(Serie serie) {
        SerieDTO dto = new SerieDTO();
        dto.setIdSerie(serie.getIdSerie());
        dto.setTitulo(serie.getTitulo());
        dto.setSinopsis(serie.getSinopsis());
        dto.setTipo(serie.getTipo());
        
        if (serie.getCreadores() != null) {
            dto.setCreadores(serie.getCreadores().stream()
                    .map(this::convertPersonaToDTO)
                    .collect(Collectors.toList()));
        }
        
        if (serie.getActores() != null) {
            dto.setActores(serie.getActores().stream()
                    .map(this::convertPersonaToDTO)
                    .collect(Collectors.toList()));
        }
        
        if (serie.getTemporadas() != null) {
            List<TemporadaDTO> temporadasDTO = serie.getTemporadas().stream()
                    .map(this::convertTemporadaToDTO)
                    .collect(Collectors.toList());
            dto.setTemporadas(temporadasDTO);
            dto.setNumeroTemporadas(temporadasDTO.size());
        }
        
        return dto;
    }

    private Serie convertToEntity(SerieDTO dto) {
        Serie serie = new Serie();
        serie.setIdSerie(dto.getIdSerie());
        serie.setTitulo(dto.getTitulo());
        serie.setSinopsis(dto.getSinopsis());
        serie.setTipo(dto.getTipo());
        // Note: Creadores, actores, temporadas would need to be set separately
        return serie;
    }

    private PersonaDTO convertPersonaToDTO(es.unican.sergio.polaflix.model.Persona persona) {
        PersonaDTO dto = new PersonaDTO();
        dto.setIdPersona(persona.getIdPersona());
        dto.setNombrePersona(persona.getNombrePersona());
        return dto;
    }

    private TemporadaDTO convertTemporadaToDTO(Temporada temporada) {
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

    private CapituloDTO convertCapituloToDTO(Capitulo capitulo) {
        CapituloDTO dto = new CapituloDTO();
        dto.setIdCap(capitulo.getIdCap());
        dto.setNumeroCap(capitulo.getNumeroCap());
        dto.setTitulo(capitulo.getTitulo());
        dto.setDescripcion(capitulo.getDescripcion());
        return dto;
    }
}