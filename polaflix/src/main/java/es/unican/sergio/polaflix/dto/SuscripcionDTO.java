package es.unican.sergio.polaflix.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuscripcionDTO {
    public interface Basic {}
    public interface Detailed extends Basic {}

    @JsonView(Basic.class)
    private Long idSuscripcion;

    @JsonView(Basic.class)
    @NotBlank(message = "El tipo de suscripción no puede estar vacío")
    private String tipo; // "SuscripcionFija" o "SuscripcionBajoDemanda"

    @JsonView(Detailed.class)
    @Positive(message = "La cuota mensual debe ser positiva")
    private Double cuotaMensual; // Para SuscripcionFija
}
