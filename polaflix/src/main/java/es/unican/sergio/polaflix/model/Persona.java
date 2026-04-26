package es.unican.sergio.polaflix.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter

public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPersona;
    
    private String nombrePersona;
}