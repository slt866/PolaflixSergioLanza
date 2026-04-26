package es.unican.sergio.polaflix.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemporadaDTO {
    public interface Basic {}
    public interface Detailed extends Basic {}

    @JsonView(Basic.class)
    private Long idTemp;

    @JsonView(Basic.class)
    private Integer numeroTemp;

    @JsonView(Basic.class)
    private String titulo;

    @JsonView(Detailed.class)
    private List<CapituloDTO> capitulos;

    @JsonView(Detailed.class)
    private Integer numeroCapitulos;
}
