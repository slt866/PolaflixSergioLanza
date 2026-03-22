import java.util.List;
import java.util.ArrayList;

// Aggregate Root
public class Usuario {
    private String id;
    private String nombreUsuario;
    private String contrasena;
    private String cuentaBancaria;
    
    // Relaciones
    private PlanSuscripcion planSuscripcion;
    private List<Visualizacion> visualizaciones;
    private List<Serie> seriesPendientes;
    private List<Serie> seriesEmpezadas;
    private List<Serie> seriesTerminadas;
    private Factura facturaActual;
    private List<Factura> facturas;

    public Usuario(String id, String nombreUsuario, String contrasena, String cuentaBancaria, PlanSuscripcion plan) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.cuentaBancaria = cuentaBancaria;
        this.planSuscripcion = plan;
        
        this.visualizaciones = new ArrayList<>();
        this.seriesPendientes = new ArrayList<>();
        this.seriesEmpezadas = new ArrayList<>();
        this.seriesTerminadas = new ArrayList<>();
        this.facturas = new ArrayList<>();
    }

    // Operaciones del Aggregate Root
    public void agregarSerie(String idSerie) {
        // TODO: Buscar la serie en el repositorio y añadir a pendientes
    }

    public Temporada ultimaTempVista(String idSerie) {
        // TODO: Lógica para recorrer "visualizaciones" y devolver la temporada mayor
        return null;
    }

    public boolean esSerieTerminada(String idSerie) {
        // TODO: Lógica interactuando con el método esUltimoCap() de la Serie
        return false;
    }
}