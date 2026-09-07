package com.colegio.dto;

import com.colegio.modelo.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** DTO de entrada/salida para el CRUD de Alumnos. */
@Getter
@Setter
public class AlumnoDTO {
    private Long idAlumno;

    @NotBlank(message = "El nombre es obligatorio") @Size(max = 50)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio") @Size(max = 50)
    private String apellido;

    @NotNull(message = "Seleccione un sexo")
    private Sexo sexo;

    @NotNull(message = "Fecha obligatoria") @Past
    private LocalDate fechaNacimiento;

    /** Id del Aula seleccionada en el formulario (el servicio resuelve la entidad). */
    private Long aulaId;

    /** Solo para mostrar en la lista (no se persiste). */
    private String aulaDescripcion;
}