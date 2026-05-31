package es.unican.sergio.polaflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.sergio.polaflix.model.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}