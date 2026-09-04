package com.colegio.dto;

import com.colegio.modelo.Sexo;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO DE REGISTRO DOCENTE.
 *
 * ¿POR QUÉ UN DTO? La entidad Profesor contiene password, roles, auditoría, etc.
 * Nunca exponemos la entidad al formulario ni al navegador: el DTO transporta
 * SOLO los datos del requisito (Nombre, Apellido, Sexo, Fecha Nacimiento,
 * correo-usuario y contraseña) desde la VISTA hasta el SERVICIO.
 *
 * Las anotaciones de jakarta.validation se evalúan con @Valid en el controlador:
 * si fallan, Spring genera errores que Thymeleaf muestra con th:errors.
 */
@Getter
@Setter
public class ProfesorRegistroDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50)
    private String apellido;

    @NotNull(message = "Seleccione un sexo")
    private Sexo sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha debe estar en el pasado")
    private LocalDate fechaNacimiento;

    /** El usuario ES el correo personal del docente (requisito). */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo válido")
    private String correo;

    private String especialidad;

    /**
     * SEGURIDAD: mínimo 8 caracteres. Esta contraseña VIAJA por HTTPS (en
     * producción) y NUNCA se persiste en texto plano: el servicio aplica BCrypt.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 60, message = "La contraseña debe tener entre 8 y 60 caracteres")
    private String password;

    /** Campo auxiliar de UI para confirmar la contraseña; no se persiste. */
    @NotBlank(message = "Confirme la contraseña")
    private String confirmarPassword;
}