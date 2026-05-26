package es.unican.sergio.polaflix.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unican.sergio.polaflix.dto.FacturaDTO;
import es.unican.sergio.polaflix.dto.ProgresoSerieDTO;
import es.unican.sergio.polaflix.dto.SerieDTO;
import es.unican.sergio.polaflix.dto.SuscripcionDTO;
import es.unican.sergio.polaflix.dto.TemporadaDTO;
import es.unican.sergio.polaflix.dto.UsuarioDTO;
import es.unican.sergio.polaflix.model.Serie;
import es.unican.sergio.polaflix.model.SuscripcionBajoDemanda;
import es.unican.sergio.polaflix.model.SuscripcionFija;
import es.unican.sergio.polaflix.model.Temporada;
import es.unican.sergio.polaflix.model.Usuario;
import es.unican.sergio.polaflix.repository.SerieRepository;
import es.unican.sergio.polaflix.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SerieRepository serieRepository;

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDTO);
    }

    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        Usuario saved = usuarioRepository.save(usuario);
        return convertToDTO(saved);
    }

    public Optional<UsuarioDTO> patch(Long id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    if (usuarioDTO.getNombreUsuario() != null) {
                        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
                    }
                    if (usuarioDTO.getCuentaBancaria() != null) {
                        usuario.setCuentaBancaria(usuarioDTO.getCuentaBancaria());
                    }
                    if (usuarioDTO.getSuscripcion() != null) {
                        usuario.setSuscripcion(convertSuscripcionToEntity(usuarioDTO.getSuscripcion()));
                    }
                    Usuario saved = usuarioRepository.save(usuario);
                    return convertToDTO(saved);
                });
    }

    public boolean deleteById(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setCuentaBancaria(usuario.getCuentaBancaria());
        if (usuario.getSuscripcion() != null) {
            dto.setSuscripcion(convertSuscripcionToDTO(usuario.getSuscripcion()));
        }
        return dto;
    }

    private Usuario convertToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCuentaBancaria(dto.getCuentaBancaria());
        if (dto.getSuscripcion() != null) {
            usuario.setSuscripcion(convertSuscripcionToEntity(dto.getSuscripcion()));
        }
        return usuario;
    }

    private es.unican.sergio.polaflix.model.Suscripcion convertSuscripcionToEntity(SuscripcionDTO dto) {
        if ("SuscripcionFija".equals(dto.getTipo())) {
            return new SuscripcionFija();
        } else if ("SuscripcionBajoDemanda".equals(dto.getTipo())) {
            return new SuscripcionBajoDemanda();
        }
        return null;
    }

    private SuscripcionDTO convertSuscripcionToDTO(es.unican.sergio.polaflix.model.Suscripcion suscripcion) {
        SuscripcionDTO dto = new SuscripcionDTO();
        dto.setIdSuscripcion(suscripcion.getIdSuscripcion());
        dto.setTipo(suscripcion.getClass().getSimpleName());
        
        if (suscripcion instanceof SuscripcionFija) {
            SuscripcionFija suscripcionFija = (SuscripcionFija) suscripcion;
            dto.setCuotaMensual(suscripcionFija.getCuotaMensual());
        }
        
        return dto;
    }

    // Métodos para gestionar series del usuario
    public Optional<UsuarioDTO> addSeriePendiente(Long usuarioId, Long serieId) {
        return usuarioRepository.findById(usuarioId)
                .flatMap(usuario -> serieRepository.findById(serieId)
                        .map(serie -> {
                            usuario.agregarSerie(serie);
                            Usuario saved = usuarioRepository.save(usuario);
                            return convertToDTO(saved);
                        }));
    }

    public Optional<List<SerieDTO>> getSeriesPendientes(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> {
                    if (usuario.getSeriesPendientes() == null) {
                        return List.of();
                    }
                    return usuario.getSeriesPendientes().stream()
                            .map(this::convertSerieToDTO)
                            .collect(Collectors.toList());
                });
    }

    public Optional<List<SerieDTO>> getSeriesEmpezadas(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> {
                    if (usuario.getSeriesEmpezadas() == null) {
                        return List.of();
                    }
                    return usuario.getSeriesEmpezadas().stream()
                            .map(this::convertSerieToDTO)
                            .collect(Collectors.toList());
                });
    }

    public Optional<List<SerieDTO>> getSeriesTerminadas(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> {
                    if (usuario.getSeriesTerminadas() == null) {
                        return List.of();
                    }
                    return usuario.getSeriesTerminadas().stream()
                            .map(this::convertSerieToDTO)
                            .collect(Collectors.toList());
                });
    }

    public Optional<UsuarioDTO> marcarCapituloVisto(Long usuarioId, Long serieId, int temp, int cap) {
        return usuarioRepository.findById(usuarioId)
                .flatMap(usuario -> serieRepository.findById(serieId)
                        .map(serie -> {
                            usuario.marcarCapituloVisto(serie, temp, cap);
                            Usuario saved = usuarioRepository.save(usuario);
                            return convertToDTO(saved);
                        }));
    }

    public Optional<List<FacturaDTO>> getFacturasDeUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> {
                    if (usuario.getFacturas() == null) {
                        return List.of();
                    }
                    return usuario.getFacturas().stream()
                            .map(this::convertFacturaToDTO)
                            .collect(Collectors.toList());
                });
    }

    public Optional<SuscripcionDTO> getSuscripcionDeUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .flatMap(usuario -> {
                    if (usuario.getSuscripcion() != null) {
                        return Optional.of(convertSuscripcionToDTO(usuario.getSuscripcion()));
                    }
                    return Optional.empty();
                });
    }

    public Optional<ProgresoSerieDTO> getProgresoSerie(Long usuarioId, Long serieId) {
        return usuarioRepository.findById(usuarioId)
                .flatMap(usuario -> serieRepository.findById(serieId)
                        .flatMap(serie -> {
                            if (usuario.getProgresosSeries() == null) {
                                return Optional.empty();
                            }
                            return usuario.getProgresosSeries().stream()
                                    .filter(progreso -> progreso.getSerie().getIdSerie().equals(serieId))
                                    .findFirst()
                                    .map(this::convertProgresoToDTO);
                        }));
    }

    public Optional<TemporadaDTO> getUltimaTempVistaDeSerie(Long usuarioId, Long serieId) {
        return usuarioRepository.findById(usuarioId)
                .flatMap(usuario -> serieRepository.findById(serieId)
                        .flatMap(serie -> {
                            Temporada temporada = usuario.ultimaTempVista(serie);
                            if (temporada != null) {
                                return Optional.of(convertTemporadaToDTO(temporada));
                            }
                            return Optional.empty();
                        }));
    }

    public Optional<Boolean> esSerieTerminadaPorUsuario(Long usuarioId, Long serieId) {
        return usuarioRepository.findById(usuarioId)
                .flatMap(usuario -> serieRepository.findById(serieId)
                        .map(serie -> usuario.esSerieTerminada(serie)));
    }

    private SerieDTO convertSerieToDTO(Serie serie) {
        SerieDTO dto = new SerieDTO();
        dto.setIdSerie(serie.getIdSerie());
        dto.setTitulo(serie.getTitulo());
        dto.setSinopsis(serie.getSinopsis());
        dto.setTipo(serie.getTipo());
        return dto;
    }

    private FacturaDTO convertFacturaToDTO(es.unican.sergio.polaflix.model.Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setIdFactura(factura.getIdFactura());
        dto.setImporteTotal(factura.getImporteTotal());
        dto.setMes(factura.getMes());
        dto.setAno(factura.getAno());
        return dto;
    }

    private ProgresoSerieDTO convertProgresoToDTO(es.unican.sergio.polaflix.model.ProgresoSerie progreso) {
        ProgresoSerieDTO dto = new ProgresoSerieDTO();
        dto.setIdProgreso(progreso.getIdProgreso());
        dto.setSerie(convertSerieToDTO(progreso.getSerie()));
        dto.setUltimaTempVista(progreso.getUltimaTempVista());
        dto.setUltimoCapVisto(progreso.getUltimoCapVisto());
        
        if (progreso.getVisualizaciones() != null) {
            dto.setVisualizaciones(progreso.getVisualizaciones().stream()
                    .map(vis -> {
                        ProgresoSerieDTO.VisualizacionDTO visDTO = new ProgresoSerieDTO.VisualizacionDTO();
                        visDTO.setNumeroTemp(vis.getNumeroTemp());
                        visDTO.setNumeroCap(vis.getNumeroCap());
                        return visDTO;
                    })
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private TemporadaDTO convertTemporadaToDTO(Temporada temporada) {
        TemporadaDTO dto = new TemporadaDTO();
        dto.setIdTemp(temporada.getIdTemp());
        dto.setNumeroTemp(temporada.getNumeroTemp());
        dto.setTitulo(temporada.getTitulo());
        return dto;
    }
}