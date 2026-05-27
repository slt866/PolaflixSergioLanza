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

/**
 * Entidad Usuario - Aggregate Root
 * 
 * Representa a un usuario registrado en el sistema Polaflix.
 * 
 * JUSTIFICACIÓN DE ESTRATEGIAS JPA:
 * 
 * @GeneratedValue(strategy = GenerationType.AUTO)
 * - Se usa AUTO para delegar la estrategia al proveedor JPA, permitiendo portabilidad
 *   entre bases de datos. Cada BD seleccionará la mejor estrategia (IDENTITY, SEQUENCE, TABLE)
 * 
 * @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
 * en progresosSeries:
 * - cascade = CascadeType.ALL: Los cambios en Usuario se propagan a ProgresoSerie
 * - orphanRemoval = true: Si se elimina un ProgresoSerie del Usuario, se elimina de BD
 * - JUSTIFICACIÓN: ProgresoSerie es una entidad débil del Usuario (no existe sin Usuario),
 *   así que los huérfanos deben ser eliminados automáticamente.
 * 
 * @ManyToMany en series (pendientes, empezadas, terminadas):
 * - Relaciones entre Usuario y Serie sin cascade ni orphanRemoval
 * - JUSTIFICACIÓN: Una Serie puede existir sin Usuario y un Usuario puede existir sin Series.
 *   No hay relación padre-hijo, solo una asociación. Los huérfanos no aplican aquí.
 *   Un Usuario puede dejar de ver una serie sin que la Serie sea eliminada.
 * 
 * @ManyToOne(cascade = CascadeType.ALL) en suscripcion:
 * - cascade = CascadeType.ALL: Los cambios en suscripción se propagan al Usuario
 * - NOTA: Esto podría revisarse - podría ser CascadeType.MERGE, CascadeType.PERSIST
 *   si la Suscripción debe ser compartida entre usuarios
 * 
 * @OneToMany(mappedBy = "usuario") en facturas:
 * - SIN cascade ni orphanRemoval
 * - JUSTIFICACIÓN: Las Facturas son históricas y no pueden ser eliminadas al eliminar Usuario
 *   (requisito legal). Se mantienen de forma permanente en la BD.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Usuario {
    /**
     * Identificador único del usuario.
     * Estrategia AUTO: Permite que el proveedor JPA elija la mejor estrategia de generación
     * de acuerdo a la base de datos utilizada.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUsuario;
    
    private String nombreUsuario;
    private String contrasena;
    private String cuentaBancaria;
    
    /**
     * Relación con la Suscripción del usuario.
     * cascade = ALL: Si el usuario es eliminado, su suscripción también.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Suscripcion suscripcion;

    /**
     * Progreso del usuario en las series vistas.
     * cascada = ALL con orphanRemoval = true porque ProgresoSerie es una entidad débil
     * del Usuario - no existe independientemente.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<ProgresoSerie> progresosSeries;
    
    /**
     * Series que el usuario tiene en lista de pendientes.
     * Sin cascade ni orphanRemoval porque:
     * - Las Series pueden existir sin Usuario (son del catálogo)
     * - Un Usuario puede dejar de ver una serie sin eliminarla del sistema
     */
    @ManyToMany 
    private List<Serie> seriesPendientes;
    
    /**
     * Series que el usuario está actualmente viendo.
     */
    @ManyToMany 
    private List<Serie> seriesEmpezadas;
    
    /**
     * Series que el usuario ha completado.
     */
    @ManyToMany 
    private List<Serie> seriesTerminadas;
    
    /**
     * Factura actual (abierta) del usuario.
     * Sin cascade porque la Factura es un documento histórico.
     */
    @OneToOne
    private Factura facturaActual;

    /**
     * Historial de todas las facturas del usuario.
     * Sin cascade ni orphanRemoval porque las Facturas son documentos históricos
     * que no deben ser eliminados incluso si se elimina el Usuario.
     */
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