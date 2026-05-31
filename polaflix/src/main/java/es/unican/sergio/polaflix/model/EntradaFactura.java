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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

public class EntradaFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEntradaFactura;
    
    private LocalDateTime fecha;
    private double cargo;
    private int numeroTemp;
    private int numeroCap;
    
    @ManyToOne
    private Factura factura;
    
    @ManyToOne
    private Serie serieFacturada;
}