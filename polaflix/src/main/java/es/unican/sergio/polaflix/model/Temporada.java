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


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Temporada {
    /**
     * Identificador único de la temporada.
     * Estrategia AUTO: Permite que el proveedor JPA elija la mejor estrategia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTemp; 
    
    private int numeroTemp;
    private String titulo;
    
    /**
     * Capítulos de esta temporada.
     * cascade = ALL con orphanRemoval = true porque los Capítulos son entidades débiles
     * de la Temporada - no pueden existir sin una Temporada padre.
     */
    @OneToMany(mappedBy = "temporada", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Capitulo> capitulos;
    
    /**
     * Serie a la que pertenece esta temporada.
     * Relación inversa de Serie.temporadas. El cascade es manejado por el lado
     * de la Serie (con @OneToMany).
     */
    @ManyToOne
    private Serie serie;

    public Temporada(int numeroTemp, String titulo, Serie serie) {
        this.numeroTemp = numeroTemp;
        this.titulo = titulo;
        this.serie = serie;
    }
}