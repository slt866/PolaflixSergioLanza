package es.unican.sergio.polaflix.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unican.sergio.polaflix.dto.CapituloDTO;
import es.unican.sergio.polaflix.dto.PersonaDTO;
import es.unican.sergio.polaflix.dto.SerieDTO;
import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.model.Capitulo;
import es.unican.sergio.polaflix.model.Serie;
import es.unican.sergio.polaflix.model.Temporada;
import es.unican.sergio.polaflix.model.TipoSerie;
import es.unican.sergio.polaflix.repository.CapituloRepository;
import es.unican.sergio.polaflix.repository.PersonaRepository;
import es.unican.sergio.polaflix.repository.SerieRepository;
import es.unican.sergio.polaflix.repository.TemporadaRepository;

@Service
@Transactional(readOnly = true)
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private CapituloRepository capituloRepository;

    @Autowired
    private TemporadaRepository temporadaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<SerieDTO> findAll() {
        return serieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SerieDTO> findById(Long id) {
        return serieRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public SerieDTO save(SerieDTO serieDTO) {
        Serie serie = convertToEntity(serieDTO);
        Serie saved = serieRepository.save(serie);
        return convertToDTO(saved);
    }

    @Transactional
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

    @Transactional
    public boolean deleteById(Long id) {
        if (serieRepository.existsById(id)) {
            serieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<SerieDTO> findByTipo(TipoSerie tipo) {
        return serieRepository.findAll().stream()
                .filter(serie -> serie.getTipo() == tipo)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SerieDTO> findByTitulo(String titulo) {
        return serieRepository.findAll().stream()
                .filter(serie -> serie.getTitulo() != null && serie.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
        if (dto.getCreadores() != null) {
            serie.setCreadores(dto.getCreadores().stream()
                    .map(pdto -> personaRepository.findById(pdto.getIdPersona()).orElse(null))
                    .filter(p -> p != null)
                    .collect(Collectors.toList()));
        }
        if (dto.getActores() != null) {
            serie.setActores(dto.getActores().stream()
                    .map(pdto -> personaRepository.findById(pdto.getIdPersona()).orElse(null))
                    .filter(p -> p != null)
                    .collect(Collectors.toList()));
        }
        if (dto.getTemporadas() != null) {
            List<Temporada> temporadas = dto.getTemporadas().stream()
                    .map(tempDTO -> {
                        Temporada temp = new Temporada();
                        temp.setIdTemp(tempDTO.getIdTemp());
                        temp.setNumeroTemp(tempDTO.getNumeroTemp());
                        temp.setTitulo(tempDTO.getTitulo());
                        temp.setSerie(serie);
                        if (tempDTO.getCapitulos() != null) {
                            List<Capitulo> capitulos = tempDTO.getCapitulos().stream()
                                    .map(capDTO -> {
                                        Capitulo cap = new Capitulo();
                                        cap.setIdCap(capDTO.getIdCap());
                                        cap.setNumeroCap(capDTO.getNumeroCap());
                                        cap.setTitulo(capDTO.getTitulo());
                                        cap.setDescripcion(capDTO.getDescripcion());
                                        cap.setTemporada(temp);
                                        return cap;
                                    }).collect(Collectors.toList());
                            temp.setCapitulos(capitulos);
                        }
                        return temp;
                    }).collect(Collectors.toList());
            serie.setTemporadas(temporadas);
        }
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

    // Métodos para gestionar temporadas de una serie
    public Optional<List<TemporadaDTO>> getTemporadasDeSerie(Long serieId) {
        return serieRepository.findById(serieId)
                .map(serie -> {
                    if (serie.getTemporadas() == null) {
                        return List.of();
                    }
                    return serie.getTemporadas().stream()
                            .map(this::convertTemporadaToDTO)
                            .collect(Collectors.toList());
                });
    }

    public Optional<TemporadaDTO> getTemporadaDeSerie(Long serieId, Long temporadaId) {
        return serieRepository.findById(serieId)
                .flatMap(serie -> temporadaRepository.findById(temporadaId)
                        .filter(temp -> temp.getSerie() != null && temp.getSerie().getIdSerie().equals(serieId))
                        .map(this::convertTemporadaToDTO));
    }

    @Transactional
    public Optional<TemporadaDTO> createTemporadaEnSerie(Long serieId, TemporadaDTO temporadaDTO) {
        return serieRepository.findById(serieId)
                .map(serie -> {
                    Temporada temporada = new Temporada();
                    temporada.setNumeroTemp(temporadaDTO.getNumeroTemp());
                    temporada.setTitulo(temporadaDTO.getTitulo());
                    temporada.setSerie(serie);
                    Temporada saved = temporadaRepository.save(temporada);
                    return convertTemporadaToDTO(saved);
                });
    }

    @Transactional
    public Optional<TemporadaDTO> updateTemporadaDeSerie(Long serieId, Long temporadaId, TemporadaDTO temporadaDTO) {
        return serieRepository.findById(serieId)
                .flatMap(serie -> temporadaRepository.findById(temporadaId)
                        .filter(temp -> temp.getSerie() != null && temp.getSerie().getIdSerie().equals(serieId))
                        .map(temporada -> {
                            if (temporadaDTO.getNumeroTemp() != null) {
                                temporada.setNumeroTemp(temporadaDTO.getNumeroTemp());
                            }
                            if (temporadaDTO.getTitulo() != null) {
                                temporada.setTitulo(temporadaDTO.getTitulo());
                            }
                            Temporada saved = temporadaRepository.save(temporada);
                            return convertTemporadaToDTO(saved);
                        }));
    }

    @Transactional
    public Optional<Boolean> deleteTemporadaDeSerie(Long serieId, Long temporadaId) {
        return serieRepository.findById(serieId)
                .flatMap(serie -> temporadaRepository.findById(temporadaId)
                        .filter(temp -> temp.getSerie() != null && temp.getSerie().getIdSerie().equals(serieId))
                        .map(temporada -> {
                            temporadaRepository.deleteById(temporadaId);
                            return true;
                        }));
    }

    // Métodos para gestionar capítulos de una temporada en una serie
    public Optional<List<CapituloDTO>> getCapitulosDeTemporada(Long serieId, Long temporadaId) {
        return serieRepository.findById(serieId)
                .flatMap(serie -> temporadaRepository.findById(temporadaId)
                        .filter(temp -> temp.getSerie() != null && temp.getSerie().getIdSerie().equals(serieId))
                        .map(temporada -> {
                            if (temporada.getCapitulos() == null) {
                                return List.of();
                            }
                            return temporada.getCapitulos().stream()
                                    .map(this::convertCapituloToDTO)
                                    .collect(Collectors.toList());
                        }));
    }

    public Optional<CapituloDTO> getCapituloDeTemporada(Long serieId, Long temporadaId, Long capituloId) {
        return serieRepository.findById(serieId)
                .flatMap(serie -> temporadaRepository.findById(temporadaId)
                        .filter(temp -> temp.getSerie() != null && temp.getSerie().getIdSerie().equals(serieId))
                        .flatMap(temporada -> capituloRepository.findById(capituloId)
                                .filter(cap -> cap.getTemporada() != null && cap.getTemporada().getIdTemp().equals(temporadaId))
                                .map(this::convertCapituloToDTO)));
    }

    @Transactional
    public Optional<CapituloDTO> createCapituloEnTemporada(Long serieId, Long temporadaId, CapituloDTO capituloDTO) {
        return serieRepository.findById(serieId)
                .flatMap(serie -> temporadaRepository.findById(temporadaId)
                        .filter(temp -> temp.getSerie() != null && temp.getSerie().getIdSerie().equals(serieId))
                        .map(temporada -> {
                            Capitulo capitulo = new Capitulo();
                            capitulo.setNumeroCap(capituloDTO.getNumeroCap());
                            capitulo.setTitulo(capituloDTO.getTitulo());
                            capitulo.setDescripcion(capituloDTO.getDescripcion());
                            capitulo.setTemporada(temporada);
                            Capitulo saved = capituloRepository.save(capitulo);
                            return convertCapituloToDTO(saved);
                        }));
    }

    @Transactional
    public Optional<CapituloDTO> updateCapituloDeTemporada(Long serieId, Long temporadaId, Long capituloId, CapituloDTO capituloDTO) {
        return serieRepository.findById(serieId)
                .flatMap(serie -> temporadaRepository.findById(temporadaId)
                        .filter(temp -> temp.getSerie() != null && temp.getSerie().getIdSerie().equals(serieId))
                        .flatMap(temporada -> capituloRepository.findById(capituloId)
                                .filter(cap -> cap.getTemporada() != null && cap.getTemporada().getIdTemp().equals(temporadaId))
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
                                    return convertCapituloToDTO(saved);
                                 }))); 
    }

    @Transactional
    public Optional<Boolean> deleteCapituloDeTemporada(Long serieId, Long temporadaId, Long capituloId) {
        return serieRepository.findById(serieId)
                .flatMap(serie -> temporadaRepository.findById(temporadaId)
                        .filter(temp -> temp.getSerie() != null && temp.getSerie().getIdSerie().equals(serieId))
                        .flatMap(temporada -> capituloRepository.findById(capituloId)
                                .filter(cap -> cap.getTemporada() != null && cap.getTemporada().getIdTemp().equals(temporadaId))
                                .map(capitulo -> {
                                    capituloRepository.deleteById(capituloId);
                                    return true;
                                })));
    }
}