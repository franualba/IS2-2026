package com.colegio.modelo;

import com.colegio.auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ENTIDAD DICTADOCLASES (clase-asociación del diagrama):
 * vincula Materia con Profesor y los Alumnos que cursan.
 * REDISEÑO: anioLectivo pasa de Date a Integer (un año no necesita hora).
 */
@Entity
@Table(name = "dictado_clases")
@Getter
@Setter
public class DictadoClases extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDictado;

    private Integer anioLectivo; // ej: 2026

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id")
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    @ManyToMany
    @JoinTable(name = "dictado_alumnos",
            joinColumns = @JoinColumn(name = "dictado_id"),
            inverseJoinColumns = @JoinColumn(name = "alumno_id"))
    private Set<Alumno> alumnos = new HashSet<>();

    /** Métodos de negocio conservados del diagrama UML. */
    public void asignarProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<Alumno> listarAlumnos() {
        return List.copyOf(alumnos);
    }
}