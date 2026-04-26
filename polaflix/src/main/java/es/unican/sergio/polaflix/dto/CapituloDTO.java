package es.unican.sergio.polaflix.dto;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapituloDTO {
    public interface Basic {}
    public interface Detailed extends Basic {}

    @JsonView(Basic.class)
    private Long idCap;

    @JsonView(Basic.class)
    private Integer numeroCap;

    @JsonView(Basic.class)
    private String titulo;

    @JsonView(Basic.class)
    private String descripcion;
}
