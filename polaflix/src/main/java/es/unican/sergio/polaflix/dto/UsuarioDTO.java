package es.unican.sergio.polaflix.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioDTO {
    public interface Basic {}
    public interface Detailed extends Basic {}

    @JsonView(Basic.class)
    private Long idUsuario;

    @JsonView(Basic.class)
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String nombreUsuario;

    @JsonView(Basic.class)
    @NotBlank(message = "La cuenta bancaria no puede estar vacía")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$", message = "Formato de IBAN inválido")
    private String cuentaBancaria;

    @JsonView(Detailed.class)
    private SuscripcionDTO suscripcion;
}