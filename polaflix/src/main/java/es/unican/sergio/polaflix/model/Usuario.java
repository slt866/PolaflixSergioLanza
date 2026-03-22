package es.unican.sergio.polaflix.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;
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
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    
    private String nombreUsuario;
    private String contrasena;
    private String cuentaBancaria;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Suscripcion suscripcion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<ProgresoSerie> progresosSeries;
    
    @ManyToMany 
    private List<Serie> seriesPendientes;
    @ManyToMany 
    private List<Serie> seriesEmpezadas;
    @ManyToMany 
    private List<Serie> seriesTerminadas;
    
    @OneToOne
    private Factura facturaActual;
    
    @OneToMany(mappedBy = "usuario")
    private List<Factura> facturas;

    // Operaciones del Aggregate Root (Reciben objetos, no IDs primitivos)
    public void agregarSerie(Serie serie) {
        // TODO: Añadir a pendientes
    }

    public void marcarCapituloVisto(Serie serie, int temp, int cap) {
        // TODO: Buscar el ProgresoSerie correspondiente, crear Visualizacion y actualizar caché
    }

    public Temporada ultimaTempVista(Serie serie) {
        // TODO: Delegar en ProgresoSerie para obtener el dato rápido
        return null;
    }

    public boolean esSerieTerminada(Serie serie) {
        // TODO: Lógica interactuando con el método esUltimoCap() de la Serie
        return false;
    }
}