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
public class ProgresoSerie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProgreso;
    
    private int ultimaTempVista;
    private int ultimoCapVisto;
    
    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Serie serie;
    
    @OneToMany(mappedBy = "progresoSerie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visualizacion> visualizaciones;
}