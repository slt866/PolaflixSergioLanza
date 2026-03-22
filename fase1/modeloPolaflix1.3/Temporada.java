import java.util.ArrayList;
import java.util.List;

public class Temporada {
    private String id;
    private int numeroTemp;
    private String titulo;
    
    // Relación de Composición hacia Capítulo
    private List<Capitulo> capitulos;

    public Temporada(String id, int numeroTemp, String titulo) {
        this.id = id;
        this.numeroTemp = numeroTemp;
        this.titulo = titulo;
        this.capitulos = new ArrayList<>();
    }
}