package es.unican.sergio.polaflix.repository;

import es.unican.sergio.polaflix.model.Temporada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporadaRepository extends JpaRepository<Temporada, Long> {
}
