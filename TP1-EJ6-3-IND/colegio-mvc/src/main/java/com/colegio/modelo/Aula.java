package com.colegio.modelo;

import com.colegio.auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** ENTIDAD AULA. Método obtenerCantidadAlumnos() conservado del diagrama. */
@Entity
@Table(name = "aulas")
@Getter
@Setter
public class Aula extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAula;

    private String division; // ej: "A", "B"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grado_id")
    private Grado grado;

    @OneToMany(mappedBy = "aula")
    private List<Alumno> alumnos = new ArrayList<>();

    /** Método de negocio conservado del diagrama UML. */
    public int obtenerCantidadAlumnos() {
        return alumnos.size();
    }
}