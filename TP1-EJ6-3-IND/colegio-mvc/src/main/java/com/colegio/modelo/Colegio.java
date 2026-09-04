package com.colegio.modelo;

import com.colegio.auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDAD COLEGIO (raíz de agregaciones del diagrama):
 *  Colegio 1 ── * Profesor | Colegio 1 ── * Materia | Colegio 1 ── * Grado
 * mappedBy indica que la FK vive en la entidad hija.
 */
@Entity
@Table(name = "colegios")
@Getter
@Setter
public class Colegio extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColegio;

    private String nombre;

    private String direccion;

    private boolean eliminado = false;

    @OneToMany(mappedBy = "colegio")
    private List<Profesor> profesores = new ArrayList<>();

    @OneToMany(mappedBy = "colegio")
    private List<Materia> materias = new ArrayList<>();

    @OneToMany(mappedBy = "colegio")
    private List<Grado> grados = new ArrayList<>();
}