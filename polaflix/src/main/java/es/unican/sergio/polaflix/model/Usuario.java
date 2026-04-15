package es.unican.sergio.polaflix.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    // Operaciones del Aggregate Root
    public void agregarSerie(Serie serie) {
        if (serie == null) {
            return;
        }
        if (containsSerie(seriesPendientes, serie) || containsSerie(seriesEmpezadas, serie) || containsSerie(seriesTerminadas, serie)) {
            return;
        }
        if (seriesPendientes == null) {
            seriesPendientes = new ArrayList<>();
        }
        seriesPendientes.add(serie);
    }

    public void marcarCapituloVisto(Serie serie, int temp, int cap) {
        if (serie == null) {
            return;
        }

        if (progresosSeries == null) {
            progresosSeries = new ArrayList<>();
        }

        ProgresoSerie progreso = buscaProgreso(serie);
        if (progreso == null) {
            progreso = new ProgresoSerie();
            progreso.setUsuario(this);
            progreso.setSerie(serie);
            progreso.setVisualizaciones(new ArrayList<>());
            progresosSeries.add(progreso);
        }

        if (progreso.getVisualizaciones() == null) {
            progreso.setVisualizaciones(new ArrayList<>());
        }

        Visualizacion visualizacion = new Visualizacion();
        visualizacion.setNumeroTemp(temp);
        visualizacion.setNumeroCap(cap);
        visualizacion.setProgresoSerie(progreso);
        progreso.getVisualizaciones().add(visualizacion);

        progreso.setUltimaTempVista(temp);
        progreso.setUltimoCapVisto(cap);

        if (seriesPendientes != null) {
            seriesPendientes.removeIf(s -> mismaSerie(s, serie));
        }

        if (esSerieTerminada(serie)) {
            if (seriesEmpezadas != null) {
                seriesEmpezadas.removeIf(s -> mismaSerie(s, serie));
            }
            if (seriesTerminadas == null) {
                seriesTerminadas = new ArrayList<>();
            }
            if (!containsSerie(seriesTerminadas, serie)) {
                seriesTerminadas.add(serie);
            }
            return;
        }

        if (!containsSerie(seriesEmpezadas, serie)) {
            if (seriesEmpezadas == null) {
                seriesEmpezadas = new ArrayList<>();
            }
            seriesEmpezadas.add(serie);
        }
    }

    public Temporada ultimaTempVista(Serie serie) {
        if (serie == null) {
            return null;
        }
        ProgresoSerie progreso = buscaProgreso(serie);
        if (progreso == null) {
            return null;
        }
        int numeroTemp = progreso.getUltimaTempVista();
        if (serie.getTemporadas() == null) {
            return null;
        }
        for (Temporada temporada : serie.getTemporadas()) {
            if (temporada != null && temporada.getNumeroTemp() == numeroTemp) {
                return temporada;
            }
        }
        return null;
    }

    public boolean esSerieTerminada(Serie serie) {
        if (serie == null) {
            return false;
        }
        ProgresoSerie progreso = buscaProgreso(serie);
        return progreso != null && serie.esUltimoCap(progreso.getUltimaTempVista(), progreso.getUltimoCapVisto());
    }

    private ProgresoSerie buscaProgreso(Serie serie) {
        if (serie == null || progresosSeries == null) {
            return null;
        }
        for (ProgresoSerie progreso : progresosSeries) {
            if (progreso != null && mismaSerie(progreso.getSerie(), serie)) {
                return progreso;
            }
        }
        return null;
    }

    private boolean containsSerie(List<Serie> lista, Serie serie) {
        if (lista == null || serie == null) {
            return false;
        }
        for (Serie item : lista) {
            if (mismaSerie(item, serie)) {
                return true;
            }
        }
        return false;
    }

    private boolean mismaSerie(Serie a, Serie b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.getIdSerie() != null && b.getIdSerie() != null) {
            return a.getIdSerie().equals(b.getIdSerie());
        }
        return a.equals(b);
    }
}