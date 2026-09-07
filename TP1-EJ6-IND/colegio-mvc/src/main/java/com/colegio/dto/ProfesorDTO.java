package com.colegio.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de SALIDA para listar profesores: expone solo datos públicos,
 * NUNCA password ni hash (principio de mínima exposición).
 */
@Getter
@Setter
public class ProfesorDTO {
    private Long idProfesor;
    private String nombre;
    private String apellido;
    private String sexo;
    private String especialidad;
    private String usuario;
}