package es.unican.sergio.polaflix.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import es.unican.sergio.polaflix.model.TipoSerie;

/**
 * DTO para actualizar una serie existente.
 * Solo para uso administrativo (backoffice).
 * Todos los campos son opcionales.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SerieUpdateDTO {

    @Size(min = 1, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
    private String titulo;

    @Size(min = 10, max = 500, message = "La sinopsis debe tener entre 10 y 500 caracteres")
    private String sinopsis;

    private TipoSerie tipo;
}
