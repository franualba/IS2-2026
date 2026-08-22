package com.sistema.gestion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * DTO - LoginForm
 * ============================================================================
 * Representa los datos ingresados en el formulario de login: el correo
 * (que funciona como usuario del sistema, segun el enunciado) y la clave.
 * ============================================================================
 */
@Getter
@Setter
public class LoginForm {

    @NotBlank(message = "Debe ingresar su correo")
    private String correo;

    @NotBlank(message = "Debe ingresar su contrasena")
    private String password;
}
