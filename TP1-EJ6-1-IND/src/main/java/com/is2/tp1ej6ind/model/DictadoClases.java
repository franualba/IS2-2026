package com.is2.tp1ej6ind.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "dictados_clases")
public class DictadoClases extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDictado;

    @Column(nullable = false)
    private LocalDate anioLectivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id")
    private Materia materia;

    public Long getId() {
        return idDictado;
    }

    public Long getIdDictado() {
        return idDictado;
    }

    public void setIdDictado(Long idDictado) {
        this.idDictado = idDictado;
    }

    public LocalDate getAnioLectivo() {
        return anioLectivo;
    }

    public void setAnioLectivo(LocalDate anioLectivo) {
        this.anioLectivo = anioLectivo;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }
}
