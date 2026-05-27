package es.unican.sergio.polaflix.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad ProgresoSerie
 * 
 * Representa el progreso de un usuario en una serie específica.
 * Registra qué capítulos ha visto y cuál fue el último capítulo visualizado.
 * 
 * Es una entidad DÉBIL, dependiente de Usuario.
 * 
 * JUSTIFICACIÓN DE ESTRATEGIAS JPA:
 * 
 * @GeneratedValue(strategy = GenerationType.AUTO)
 * - Se usa AUTO para permitir portabilidad entre bases de datos.
 * 
 * @OneToMany(mappedBy = "progresoSerie", cascade = CascadeType.ALL, orphanRemoval = true)
 * en visualizaciones:
 * - cascade = CascadeType.ALL: Los cambios se propagan a Visualizacion
 * - orphanRemoval = true: Si se elimina una Visualizacion del ProgresoSerie, se elimina de BD
 * - JUSTIFICACIÓN: Las Visualizaciones son entidades débiles del ProgresoSerie.
 *   Una Visualización no tiene sentido sin el contexto del ProgresoSerie.
 *   Los huérfanos deben ser eliminados automáticamente.
 * 
 * @ManyToOne en usuario y serie:
 * - SIN cascade
 * - JUSTIFICACIÓN: El Usuario padre define el cascade desde su lado con orphanRemoval = true.
 *   El lado inverso (ManyToOne) no debe definir cascade para evitar conflictos.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class ProgresoSerie {
    /**
     * Identificador único del progreso.
     * Estrategia AUTO: Permite que el proveedor JPA elija la mejor estrategia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProgreso;
    
    /**
     * Número de la última temporada vista por el usuario.
     */
    private int ultimaTempVista;
    
    /**
     * Número del último capítulo visto en la última temporada.
     */
    private int ultimoCapVisto;
    
    /**
     * Usuario a cuyo progreso corresponde este registro.
     * Relación inversa de Usuario.progresosSeries.
     */
    @ManyToOne
    private Usuario usuario;
    
    /**
     * Serie del progreso.
     * Relación inversa: ProgresoSerie pertenece a una Serie.
     */
    @ManyToOne
    private Serie serie;
    
    /**
     * Visualizaciones registradas del usuario en esta serie.
     * cascade = ALL con orphanRemoval = true porque las Visualizaciones son entidades débiles
     * del ProgresoSerie - no pueden existir sin un ProgresoSerie padre.
     */
    @OneToMany(mappedBy = "progresoSerie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visualizacion> visualizaciones;
}