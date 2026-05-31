package es.unican.sergio.polaflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.sergio.polaflix.model.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
