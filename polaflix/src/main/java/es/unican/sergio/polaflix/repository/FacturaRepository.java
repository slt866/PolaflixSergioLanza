package es.unican.sergio.polaflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.sergio.polaflix.model.Factura;

// Repositorio para el Aggregate Root de la facturación
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
