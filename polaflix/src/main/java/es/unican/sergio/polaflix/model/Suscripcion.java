package es.unican.sergio.polaflix.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_suscripcion")
@Getter
@Setter
public abstract class Suscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSuscripcion;
    
    public abstract double calcularCosteFinal(double costeVisualizaciones);
}