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

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
// Aggregate Root
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSerie;
    
    private String titulo;
    private String sinopsis;
    
    @ManyToMany
    private List<Persona> creadores;
    
    @ManyToMany
    private List<Persona> actores;
    
    @Enumerated(EnumType.STRING)
    private TipoSerie tipo;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Temporada> temporadas;

    // Operaciones del Aggregate Root
    public Capitulo getCapitulo(int numeroTemp, int numeroCap) {
        // TODO: Implementar búsqueda
        return null; 
    }

    public boolean esUltimoCap(int numeroTemp, int numeroCap) {
        // TODO: Implementar lógica
        return false; 
    }
}