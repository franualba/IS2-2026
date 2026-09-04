package com.colegio.modelo;

import com.colegio.auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDAD GRADO. Composición con Aula (CascadeType.ALL + orphanRemoval):
 * si se elimina un Grado se eliminan sus Aulas (semántica de composición UML ◆).
 */
@Entity
@Table(name = "grados")
@Getter
@Setter
public class Grado extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrado;

    private String nivel; // ej: "1er Año"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id")
    private Colegio colegio;

    @OneToMany(mappedBy = "grado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aula> aulas = new ArrayList<>();

    /** Método de negocio conservado del diagrama UML. */
    public void agregarAula(Aula aula) {
        aulas.add(aula);
        aula.setGrado(this);
    }
}