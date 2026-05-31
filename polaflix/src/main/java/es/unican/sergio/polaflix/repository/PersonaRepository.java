package es.unican.sergio.polaflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.sergio.polaflix.model.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}