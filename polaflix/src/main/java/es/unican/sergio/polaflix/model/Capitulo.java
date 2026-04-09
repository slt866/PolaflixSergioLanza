package es.unican.sergio.polaflix.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Capitulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCap;
    private int numeroCap;
    private String titulo;
    private String descripcion;
    
    @ManyToOne
    private Temporada temporada;
}