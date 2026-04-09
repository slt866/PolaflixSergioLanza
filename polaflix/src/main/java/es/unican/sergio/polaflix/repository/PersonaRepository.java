package es.unican.sergio.polaflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.sergio.polaflix.model.Persona;

// Repositorio necesario para persistir a los creadores/actores 
// antes de asociarlos a una serie.
public interface PersonaRepository extends JpaRepository<Persona, Long> {
}