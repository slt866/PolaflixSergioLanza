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
    /**
     * Identificador único de la suscripción.
     * Estrategia AUTO: Permite que el proveedor JPA elija la mejor estrategia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSuscripcion;
    
    /**
     * Calcula el coste final de la suscripción en el período actual.
     * 
     * @param costeVisualizaciones costo total de las visualizaciones del período
     * @return el coste final según el tipo de suscripción
     */
    public abstract double calcularCosteFinal(double costeVisualizaciones);
}