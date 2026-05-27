package es.unican.sergio.polaflix.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para actualizar un usuario existente.
 * Todos los campos son opcionales (pueden actualizar solo lo que necesiten).
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioUpdateDTO {

    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String nombreUsuario;

    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String contrasena;

    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$", message = "Formato de IBAN inválido")
    private String cuentaBancaria;

    private Long idSuscripcion;
}
