import java.util.ArrayList;
import java.util.List;

public class Serie {
    private String id;
    private String titulo;
    private String sinopsis;
    private List<String> creadores;
    private List<String> actores;
    
    // Relaciones
    private TipoSerie tipo;
    private List<Temporada> temporadas;

    public Serie(String id, String titulo, String sinopsis, TipoSerie tipo) {
        this.id = id;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.tipo = tipo;
        this.creadores = new ArrayList<>();
        this.actores = new ArrayList<>();
        this.temporadas = new ArrayList<>();
    }

    // Operaciones del Aggregate Root
    public Capitulo getCapitulo(int numeroTemp, int numeroCap) {
        // Lógica analítica: Buscar la temporada y luego el capítulo
        // (Se implementaría recorriendo las listas)
        return null; // TODO: Implementar búsqueda
    }

    public boolean esUltimoCap(int numeroTemp, int numeroCap) {
        // Lógica: Comprobar si coincide con el último elemento de la última temporada
        return false; // TODO: Implementar lógica
    }
}