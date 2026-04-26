package es.unican.sergio.polaflix.service;

import es.unican.sergio.polaflix.dto.UsuarioDTO;
import es.unican.sergio.polaflix.dto.SuscripcionDTO;
import es.unican.sergio.polaflix.model.Usuario;
import es.unican.sergio.polaflix.model.SuscripcionFija;
import es.unican.sergio.polaflix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
        // Note: Suscripcion would need to be set separately or handled
        return usuario;
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
}