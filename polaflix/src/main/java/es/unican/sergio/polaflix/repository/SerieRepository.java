package es.unican.sergio.polaflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.sergio.polaflix.model.Serie;

// Repositorio para el Aggregate Root del catálogo de contenidos
public interface SerieRepository extends JpaRepository<Serie, Long> {
}