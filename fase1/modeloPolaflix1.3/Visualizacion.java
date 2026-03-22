import java.time.LocalDateTime;
// Value Object
public class Visualizacion {
    private int numeroTemp;
    private int numeroCap;
    private LocalDateTime fecha;
    private double cargo;
    
    // Relación hacia Serie (Referencia desde el exterior del Agregado)
    private Serie serieVisualizada;

    public Visualizacion(int numeroTemp, int numeroCap, LocalDateTime fecha, double cargo, Serie serieVisualizada) {
        this.numeroTemp = numeroTemp;
        this.numeroCap = numeroCap;
        this.fecha = fecha;
        this.cargo = cargo;
        this.serieVisualizada = serieVisualizada;
    }
}
