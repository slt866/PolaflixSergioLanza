import java.time.LocalDateTime;

// Value Object (Snapshot)
public class EntradaFactura {
    private LocalDateTime fecha;
    private double cargo;
    private int numeroTemp;
    private int numeroCap;
    
    // Relación hacia Serie
    private Serie serieFacturada;

    public EntradaFactura(LocalDateTime fecha, double cargo, int numeroTemp, int numeroCap, Serie serieFacturada) {
        this.fecha = fecha;
        this.cargo = cargo;
        this.numeroTemp = numeroTemp;
        this.numeroCap = numeroCap;
        this.serieFacturada = serieFacturada;
    }

    public double getCargo() {
        return cargo;
    }
}