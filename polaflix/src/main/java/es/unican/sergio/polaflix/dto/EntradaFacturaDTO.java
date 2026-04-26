package es.unican.sergio.polaflix.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EntradaFacturaDTO {
    @JsonView(FacturaDTO.Basic.class)
    private Long idEntradaFactura;

    @JsonView(FacturaDTO.Basic.class)
    @NotNull(message = "La fecha no puede ser nula")
    private LocalDateTime fecha;

    @JsonView(FacturaDTO.Basic.class)
    @NotNull(message = "El cargo no puede ser nulo")
    @Positive(message = "El cargo debe ser positivo")
    private double cargo;

    @JsonView(FacturaDTO.Basic.class)
    @NotNull(message = "El número de temporada no puede ser nulo")
    @Positive(message = "El número de temporada debe ser positivo")
    private int numeroTemp;

    @JsonView(FacturaDTO.Basic.class)
    @NotNull(message = "El número de capítulo no puede ser nulo")
    @Positive(message = "El número de capítulo debe ser positivo")
    private int numeroCap;

    @JsonView(FacturaDTO.Basic.class)
    @NotBlank(message = "El título de la serie no puede estar vacío")
    private String serieTitulo;
}