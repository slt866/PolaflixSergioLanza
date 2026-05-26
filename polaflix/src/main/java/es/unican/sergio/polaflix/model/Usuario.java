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

import es.unican.sergio.polaflix.model.Visualizacion;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
// Aggregate Root
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Usuario(String nombreUsuario, String contrasena, String cuentaBancaria, Suscripcion suscripcion) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.cuentaBancaria = cuentaBancaria;
        this.suscripcion = suscripcion;
    }

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

        Visualizacion existente = buscaVisualizacion(progreso, temp, cap);
        if (existente != null) {
            progreso.getVisualizaciones().remove(existente);
            if (progreso.getVisualizaciones().isEmpty()) {
                progreso.setUltimaTempVista(0);
                progreso.setUltimoCapVisto(0);
                moverSerieAPendientes(serie);
                return;
            }
            actualizarUltimoCapVisto(progreso);
            actualizarEstadoSerie(serie, progreso);
            return;
        }

        Visualizacion visualizacion = new Visualizacion();
        visualizacion.setNumeroTemp(temp);
        visualizacion.setNumeroCap(cap);
        visualizacion.setProgresoSerie(progreso);
        progreso.getVisualizaciones().add(visualizacion);

        actualizarUltimoCapVisto(progreso);
        if (seriesPendientes != null) {
            seriesPendientes.removeIf(s -> mismaSerie(s, serie));
        }
        actualizarEstadoSerie(serie, progreso);
    }

    private Visualizacion buscaVisualizacion(ProgresoSerie progreso, int temp, int cap) {
        if (progreso == null || progreso.getVisualizaciones() == null) {
            return null;
        }
        for (Visualizacion visualizacion : progreso.getVisualizaciones()) {
            if (visualizacion != null && visualizacion.getNumeroTemp() == temp && visualizacion.getNumeroCap() == cap) {
                return visualizacion;
            }
        }
        return null;
    }

    private void actualizarUltimoCapVisto(ProgresoSerie progreso) {
        int maxTemp = 0;
        int maxCap = 0;
        for (Visualizacion visualizacion : progreso.getVisualizaciones()) {
            if (visualizacion == null) {
                continue;
            }
            if (visualizacion.getNumeroTemp() > maxTemp ||
                    (visualizacion.getNumeroTemp() == maxTemp && visualizacion.getNumeroCap() > maxCap)) {
                maxTemp = visualizacion.getNumeroTemp();
                maxCap = visualizacion.getNumeroCap();
            }
        }
        progreso.setUltimaTempVista(maxTemp);
        progreso.setUltimoCapVisto(maxCap);
    }

    private void actualizarEstadoSerie(Serie serie, ProgresoSerie progreso) {
        if (seriesPendientes != null) {
            seriesPendientes.removeIf(s -> mismaSerie(s, serie));
        }
        if (seriesEmpezadas != null) {
            seriesEmpezadas.removeIf(s -> mismaSerie(s, serie));
        }
        if (seriesTerminadas != null) {
            seriesTerminadas.removeIf(s -> mismaSerie(s, serie));
        }

        if (progreso.getVisualizaciones() == null || progreso.getVisualizaciones().isEmpty()) {
            moverSerieAPendientes(serie);
            return;
        }

        if (serie.esUltimoCap(progreso.getUltimaTempVista(), progreso.getUltimoCapVisto())) {
            if (seriesTerminadas == null) {
                seriesTerminadas = new ArrayList<>();
            }
            if (!containsSerie(seriesTerminadas, serie)) {
                seriesTerminadas.add(serie);
            }
        } else {
            if (seriesEmpezadas == null) {
                seriesEmpezadas = new ArrayList<>();
            }
            if (!containsSerie(seriesEmpezadas, serie)) {
                seriesEmpezadas.add(serie);
            }
        }
    }

    private void moverSerieAPendientes(Serie serie) {
        if (seriesEmpezadas != null) {
            seriesEmpezadas.removeIf(s -> mismaSerie(s, serie));
        }
        if (seriesTerminadas != null) {
            seriesTerminadas.removeIf(s -> mismaSerie(s, serie));
        }
        if (seriesPendientes == null) {
            seriesPendientes = new ArrayList<>();
        }
        if (!containsSerie(seriesPendientes, serie)) {
            seriesPendientes.add(serie);
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