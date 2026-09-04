package com.colegio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la funcionalidad "CAMBIAR CONTRASEÑA".
 * Incluye la contraseña ACTUAL (para verificar identidad del usuario) y la nueva.
 */
@Getter
@Setter
public class CambioPasswordDTO {

    @NotBlank(message = "Ingrese su contraseña actual")
    private String passwordActual;

    @NotBlank(message = "Ingrese la nueva contraseña")
    @Size(min = 8, max = 60, message = "La nueva contraseña debe tener entre 8 y 60 caracteres")
    private String passwordNueva;

    @NotBlank(message = "Confirme la nueva contraseña")
    private String confirmarPasswordNueva;
}