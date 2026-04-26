package es.unican.sergio.polaflix.repository;

import es.unican.sergio.polaflix.model.Capitulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapituloRepository extends JpaRepository<Capitulo, Long> {
}
