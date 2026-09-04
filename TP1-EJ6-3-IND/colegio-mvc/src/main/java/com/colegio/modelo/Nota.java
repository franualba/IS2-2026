package com.colegio.modelo;

import com.colegio.auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ENTIDAD NOTA: Materia 1 ── * Nota; y pertenece a un Alumno.
 * Permite implementar listarNotas() del diagrama.
 */
@Entity
@Table(name = "notas")
@Getter
@Setter
public class Nota extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNota;

    private LocalDate fecha;

    private float valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id")
    private Materia materia;
}