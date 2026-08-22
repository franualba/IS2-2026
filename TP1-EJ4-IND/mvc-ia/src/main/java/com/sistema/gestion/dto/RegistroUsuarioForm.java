package com.sistema.gestion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * ============================================================================
 * DTO (Data Transfer Object) - RegistroUsuarioForm
 * ============================================================================
 * Objeto de transferencia usado exclusivamente por la VISTA (formulario
 * Thymeleaf de registro) y el CONTROLADOR (AuthController), para no exponer
 * directamente la entidad JPA "Usuario" en el formulario HTML (buena
 * practica: separar el modelo de persistencia del modelo de presentacion).
 *
 * Contiene los datos personales solicitados por el enunciado: Nombre,
 * Apellido, Documento, Fecha de Nacimiento y Correo Personal, mas la
 * contrasena de acceso.
 * ============================================================================
 */
@Getter
@Setter
public class RegistroUsuarioForm {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaDeNacimiento;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido")
    private String correo;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 4, message = "La contrasena debe tener al menos 4 caracteres")
    private String password;
}
