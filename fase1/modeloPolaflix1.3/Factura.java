import java.util.ArrayList;
import java.util.List;

// Aggregate Root
public class Factura {
    private String id;
    private double importeTotal;
    private int mes;
    private int ano;
    
    // Composición de entradas
    private List<EntradaFactura> entradas;

    public Factura(String id, int mes, int ano) {
        this.id = id;
        this.mes = mes;
        this.ano = ano;
        this.importeTotal = 0.0;
        this.entradas = new ArrayList<>();
    }
    
    public void calcularImporteTotal() {
        this.importeTotal = this.entradas.stream()
                                         .mapToDouble(e -> e.getCargo())
                                         .sum();
    }
}
