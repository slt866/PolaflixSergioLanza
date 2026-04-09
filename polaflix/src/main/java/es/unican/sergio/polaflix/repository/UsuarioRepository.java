package es.unican.sergio.polaflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.sergio.polaflix.model.Usuario;

// Heredar de JpaRepository le dice a Spring que genere todo el SQL automáticamente
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Aquí podríamos añadir consultas personalizadas en el futuro, por ejemplo:
    // Usuario findByNombreUsuario(String nombreUsuario);
}