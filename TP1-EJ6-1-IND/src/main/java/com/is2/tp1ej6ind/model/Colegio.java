package com.is2.tp1ej6ind.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colegios")
public class Colegio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColegio;

    @NotBlank(message = "El nombre del colegio es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @OneToMany(mappedBy = "colegio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grado> grados = new ArrayList<>();

    @OneToMany(mappedBy = "colegio", cascade = CascadeType.ALL)
    private List<Profesor> profesores = new ArrayList<>();

    @OneToMany(mappedBy = "colegio", cascade = CascadeType.ALL)
    private List<Materia> materias = new ArrayList<>();

    public Long getId() {
        return idColegio;
    }

    public Long getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(Long idColegio) {
        this.idColegio = idColegio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Grado> getGrados() {
        return grados;
    }

    public void setGrados(List<Grado> grados) {
        this.grados = grados;
    }

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<Profesor> profesores) {
        this.profesores = profesores;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }
}
