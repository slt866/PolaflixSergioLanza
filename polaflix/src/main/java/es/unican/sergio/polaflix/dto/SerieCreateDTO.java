package es.unican.sergio.polaflix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import es.unican.sergio.polaflix.model.TipoSerie;

/**
 * DTO para crear una nueva serie.
 * Solo para uso administrativo (backoffice).
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SerieCreateDTO {

    @NotBlank(message = "El título de la serie no puede estar vacío")
    @Size(min = 1, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
    private String titulo;

    @NotBlank(message = "La sinopsis no puede estar vacía")
    @Size(min = 10, max = 500, message = "La sinopsis debe tener entre 10 y 500 caracteres")
    private String sinopsis;

    @NotNull(message = "El tipo de serie no puede ser nulo")
    private TipoSerie tipo;
}
