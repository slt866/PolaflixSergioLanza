package es.unican.sergio.polaflix.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad Serie - Aggregate Root
 * 
 * Representa una serie de televisión en el catálogo de Polaflix.
 * 
 * JUSTIFICACIÓN DE ESTRATEGIAS JPA:
 * 
 * @GeneratedValue(strategy = GenerationType.AUTO)
 * - Se usa AUTO para permitir que el proveedor JPA elija la mejor estrategia
 *   según la base de datos (IDENTITY, SEQUENCE, TABLE), mejorando la portabilidad.
 * 
 * @Enumerated(EnumType.STRING)
 * - Se usa STRING en lugar de ORDINAL para almacenar el tipo de serie por nombre
 * - JUSTIFICACIÓN: Proporciona mejor legibilidad en la base de datos y evita problemas
 *   si se cambia el orden de los valores del enum en el futuro.
 * 
 * @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, orphanRemoval = true)
 * en temporadas:
 * - cascade = CascadeType.ALL: Los cambios en Serie se propagan a Temporada
 * - orphanRemoval = true: Si se elimina una Temporada de la Serie, se elimina de BD
 * - JUSTIFICACIÓN: Las Temporadas son entidades débiles de la Serie (solo existen
 *   como parte de una Serie). Los huérfanos deben ser eliminados automáticamente.
 * 
 * @ManyToMany en creadores y actores:
 * - SIN cascade ni orphanRemoval
 * - JUSTIFICACIÓN: Las Personas (creadores/actores) son independientes de las Series.
 *   Una Persona puede ser creador/actor de múltiples series. Un cambio en una Persona
 *   no debe afectar todas sus series, y viceversa.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Serie {
    /**
     * Identificador único de la serie.
     * Estrategia AUTO: Permite que el proveedor JPA elija la mejor estrategia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSerie;
    
    private String titulo;
    private String sinopsis;
    
    /**
     * Creadores de la serie (directores, productores, etc.).
     * Sin cascade ni orphanRemoval porque las Personas pueden ser compartidas
     * entre múltiples series.
     */
    @ManyToMany
    private List<Persona> creadores;
    
    /**
     * Actores/Actrices de la serie.
     */
    @ManyToMany
    private List<Persona> actores;
    
    /**
     * Tipo de serie: COMEDIA, DRAMA, ACCION, TERROR, ROMANCE, etc.
     * Se almacena como STRING en la BD para mejor legibilidad y mantenibilidad.
     */
    @Enumerated(EnumType.STRING)
    private TipoSerie tipo;

    /**
     * Temporadas de la serie.
     * cascade = ALL con orphanRemoval = true porque las Temporadas son entidades débiles
     * de la Serie - no pueden existir sin una Serie padre.
     */
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Temporada> temporadas;

    public Serie(String titulo, String sinopsis, TipoSerie tipo) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.tipo = tipo;
    }

    // Operaciones del Aggregate Root
    public Capitulo getCapitulo(int numeroTemp, int numeroCap) {
        if (temporadas == null) {
            return null;
        }
        for (Temporada temporada : temporadas) {
            if (temporada == null || temporada.getNumeroTemp() != numeroTemp || temporada.getCapitulos() == null) {
                continue;
            }
            for (Capitulo capitulo : temporada.getCapitulos()) {
                if (capitulo != null && capitulo.getNumeroCap() == numeroCap) {
                    return capitulo;
                }
            }
        }
        return null;
    }

    public boolean esUltimoCap(int numeroTemp, int numeroCap) {
        if (temporadas == null || temporadas.isEmpty()) {
            return false;
        }
        Temporada ultimaTemporada = null;
        for (Temporada temporada : temporadas) {
            if (temporada == null) {
                continue;
            }
            if (ultimaTemporada == null || temporada.getNumeroTemp() > ultimaTemporada.getNumeroTemp()) {
                ultimaTemporada = temporada;
            }
        }
        if (ultimaTemporada == null || ultimaTemporada.getCapitulos() == null || ultimaTemporada.getCapitulos().isEmpty()) {
            return false;
        }
        int maxCapitulo = -1;
        for (Capitulo capitulo : ultimaTemporada.getCapitulos()) {
            if (capitulo != null && capitulo.getNumeroCap() > maxCapitulo) {
                maxCapitulo = capitulo.getNumeroCap();
            }
        }
        return ultimaTemporada.getNumeroTemp() == numeroTemp && maxCapitulo == numeroCap;
    }
}