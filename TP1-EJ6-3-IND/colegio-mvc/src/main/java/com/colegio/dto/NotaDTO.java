package com.colegio.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** DTO de entrada/salida para el CRUD de Notas (implementa listarNotas del diagrama). */
@Getter
@Setter
public class NotaDTO {
    private Long idNota;

    @NotNull(message = "Ingrese el valor")
    @DecimalMin(value = "0", message = "Mínimo 0")
    @DecimalMax(value = "10", message = "Máximo 10")
    private Float valor;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "Seleccione alumno")
    private Long alumnoId;

    @NotNull(message = "Seleccione materia")
    private Long materiaId;

    private String alumnoNombre;   // solo vista
    private String materiaNombre;  // solo vista
}