package es.unican.sergio.polaflix.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("BAJO_DEMANDA")
public class SuscripcionBajoDemanda extends Suscripcion {

    @Override
    public double calcularCosteFinal(double costeVisualizaciones) {
        // Cobra exactamente lo que ha consumido (polimorfismo puro)
        return costeVisualizaciones;
    }
}