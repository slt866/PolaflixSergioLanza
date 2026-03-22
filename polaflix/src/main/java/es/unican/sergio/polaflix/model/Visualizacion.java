package es.unican.sergio.polaflix.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Visualizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVisualizacion;
    
    private int numeroTemp;
    private int numeroCap;
    private LocalDateTime fecha;
    private double cargo;
    
    @ManyToOne
    private ProgresoSerie progresoSerie;
}