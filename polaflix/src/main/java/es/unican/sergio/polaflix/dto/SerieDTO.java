package es.unican.sergio.polaflix.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import es.unican.sergio.polaflix.model.TipoSerie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SerieDTO {
    public interface Basic {}
    public interface Detailed extends Basic {}

    @JsonView(Basic.class)
    private Long idSerie;

    @JsonView(Basic.class)
    @NotBlank(message = "El título de la serie no puede estar vacío")
    @Size(min = 1, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
    private String titulo;

    @JsonView(Basic.class)
    @NotBlank(message = "La sinopsis no puede estar vacía")
    @Size(min = 10, max = 500, message = "La sinopsis debe tener entre 10 y 500 caracteres")
    private String sinopsis;

    @JsonView(Basic.class)
    @NotNull(message = "El tipo de serie no puede ser nulo")
    private TipoSerie tipo;

    @JsonView(Detailed.class)
    private List<PersonaDTO> creadores;

    @JsonView(Detailed.class)
    private List<PersonaDTO> actores;

    @JsonView(Detailed.class)
    private List<TemporadaDTO> temporadas;

    @JsonView(Detailed.class)
    private Integer numeroTemporadas;
}