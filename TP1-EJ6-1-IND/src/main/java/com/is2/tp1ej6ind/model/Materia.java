package com.is2.tp1ej6ind.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "materias")
public class Materia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateria;

    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id")
    private Colegio colegio;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Nota> notas = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DictadoClases> dictados = new java.util.ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    public Long getId() {
        return idMateria;
    }

    public Long getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Long idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    public java.util.List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(java.util.List<Nota> notas) {
        this.notas = notas;
    }

    public java.util.List<DictadoClases> getDictados() {
        return dictados;
    }

    public void setDictados(java.util.List<DictadoClases> dictados) {
        this.dictados = dictados;
    }
}
