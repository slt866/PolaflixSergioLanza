package es.unican.sergio.polaflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.sergio.polaflix.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}