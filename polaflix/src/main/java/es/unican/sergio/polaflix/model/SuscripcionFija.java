package es.unican.sergio.polaflix.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUOTA_FIJA")
public class SuscripcionFija extends Suscripcion {

    private final double cuotaMensual;

    public SuscripcionFija() {
        this.cuotaMensual = 20.0;
    }

    @Override
    public double calcularCosteFinal(double costeVisualizaciones) {
        return this.cuotaMensual;
    }
}