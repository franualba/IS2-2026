package com.colegio.modelo;

import com.colegio.auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** ENTIDAD MATERIA: Colegio 1 ── * Materia; participa en Dictados y Notas. */
@Entity
@Table(name = "materias")
@Getter
@Setter
public class Materia extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateria;

    private String nombre;

    private boolean eliminado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id")
    private Colegio colegio;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    private List<DictadoClases> dictados = new ArrayList<>();

    @OneToMany(mappedBy = "materia")
    private List<Nota> notas = new ArrayList<>();
}