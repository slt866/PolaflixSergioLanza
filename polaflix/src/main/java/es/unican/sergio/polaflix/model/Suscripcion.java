package es.unican.sergio.polaflix.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad Suscripcion (Clase base abstracta)
 * 
 * Representa la suscripción de un usuario a la plataforma.
 * Existen dos tipos de suscripciones concretas:
 * - SuscripcionFija: Pago mensual fijo
 * - SuscripcionBajoDemanda: Pago por visualización
 * 
 * JUSTIFICACIÓN DE ESTRATEGIA DE HERENCIA (SINGLE_TABLE):
 * 
 * Se utiliza @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
 * 
 * Ventajas de SINGLE_TABLE:
 * 1. Mejor rendimiento: Solo una tabla para todas las clases de la jerarquía
 * 2. Queries simples: No requiere JOINs para traer objetos polimórficos
 * 3. Transacciones más rápidas: Menos operaciones en BD
 * 
 * Desventajas (aceptadas en este caso):
 * 1. Columnas NULL: Las columnas de tipos específicos tendrán valores NULL
 * 2. Menos normalización: Todos los campos en una tabla
 * 
 * Decisión: SINGLE_TABLE es la mejor opción porque:
 * - Hay solo dos tipos de suscripción (pocos subtipos)
 * - Los usuarios consultan su suscripción frecuentemente
 * - No hay muchas columnas específicas de cada subtipo
 * - La simpleza es más importante que la normalización perfecta
 * 
 * ALTERNATIVAS CONSIDERADAS:
 * - JOINED: Requeriría JOINs en cada query de Usuario, afectando rendimiento
 * - TABLE_PER_CLASS: Tendría un UUID que debe ser único en múltiples tablas
 * 
 * @DiscriminatorColumn(name = "tipo_suscripcion")
 * - Columna que identifica qué tipo de suscripción es (FIJA, BAJO_DEMANDA)
 * - Se rellena automáticamente con el valor del @DiscriminatorValue de la subclase
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_suscripcion")
@Getter
@Setter
public abstract class Suscripcion {
    /**
     * Identificador único de la suscripción.
     * Estrategia AUTO: Permite que el proveedor JPA elija la mejor estrategia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSuscripcion;
    
    /**
     * Calcula el coste final de la suscripción en el período actual.
     * 
     * @param costeVisualizaciones costo total de las visualizaciones del período
     * @return el coste final según el tipo de suscripción
     */
    public abstract double calcularCosteFinal(double costeVisualizaciones);
}