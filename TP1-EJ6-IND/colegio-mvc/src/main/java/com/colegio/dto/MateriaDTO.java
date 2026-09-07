package com.colegio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** DTO de entrada/salida para el CRUD de Materias. */
@Getter
@Setter
public class MateriaDTO {
    private Long idMateria;

    @NotBlank(message = "El nombre es obligatorio") @Size(max = 80)
    private String nombre;

    private Long colegioId;
    private String colegioNombre; // solo vista
}