package es.unican.sergio.polaflix.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FacturaDTO {
    public interface Basic {}
    public interface Detailed extends Basic {}

    @JsonView(Basic.class)
    private Long idFactura;

    @JsonView(Basic.class)
    @NotNull(message = "El importe no puede ser nulo")
    @Positive(message = "El importe debe ser positivo")
    private Double importeTotal;

    @JsonView(Basic.class)
    @NotNull(message = "El mes no puede ser nulo")
    @Min(value = 1, message = "El mes debe estar entre 1 y 12")
    @Max(value = 12, message = "El mes debe estar entre 1 y 12")
    private Integer mes;

    @JsonView(Basic.class)
    @NotNull(message = "El año no puede ser nulo")
    @Min(value = 2000, message = "El año debe ser mayor a 2000")
    private Integer ano;

    @JsonView(Detailed.class)
    private List<EntradaFacturaDTO> entradas;

    @JsonView(Detailed.class)
    private String usuarioNombre;
}