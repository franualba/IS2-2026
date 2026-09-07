package com.colegio.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * ENTIDAD ALUMNO (hereda de Persona).
 * Relación con Aula del diagrama: Aula 1 ── 1..* Alumno.
 */
@Entity
@Table(name = "alumnos")
@Getter
@Setter
public class Alumno extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlumno;

    /** Eliminación lógica. */
    private boolean eliminado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    private Aula aula;
}