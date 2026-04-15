package es.unican.sergio.polaflix.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
// Aggregate Root
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;
    
    private double importeTotal;
    private int mes;
    private int ano;
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntradaFactura> entradas;
    
    @ManyToOne
    private Usuario usuario;
    
    public void calcularImporteTotal() {
        double costeVisualizaciones = 0.0;
        if (entradas != null) {
            for (EntradaFactura entrada : entradas) {
                if (entrada != null) {
                    costeVisualizaciones += entrada.getCargo();
                }
            }
        }

        if (usuario != null && usuario.getSuscripcion() != null) {
            importeTotal = usuario.getSuscripcion().calcularCosteFinal(costeVisualizaciones);
        } else {
            importeTotal = costeVisualizaciones;
        }
    }
}