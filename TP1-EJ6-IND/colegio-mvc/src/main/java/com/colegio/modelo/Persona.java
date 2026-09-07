package com.colegio.modelo;

import com.colegio.auditoria.AuditoriaEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * CLASE PADRE del diagrama (herencia "Extends" de Profesor y Alumno).
 *
 * REDISEÑO: se agregan "sexo" y "fechaNacimiento" aquí, porque el requisito de
 * registro docente los exige y además son útiles para Alumno.
 *
 * @MappedSuperclass: no genera tabla propia; sus columnas se replican en las
 * tablas hijas (profesores, alumnos). Estrategia simple y eficiente cuando no
 * se necesitan polimorfismo ni consultas sobre "Persona" en sí misma.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Persona extends AuditoriaEntity {

    private String nombre;

    private String apellido;

    /** Nuevo campo del rediseño (requisito de registro). */
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    /** Nuevo campo del rediseño (requisito de registro). */
    private LocalDate fechaNacimiento;
}