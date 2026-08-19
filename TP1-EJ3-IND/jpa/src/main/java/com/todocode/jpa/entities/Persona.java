package com.todocode.jpa.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "personas")
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "documento", length = 20, unique = true)
    private String documento;

    // Relación UNO A UNO con Auto (Una persona tiene UN auto)
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Auto auto;

    // Constructores
    public Persona() {
    }

    public Persona(String nombre, String apellido, String documento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Auto getAuto() {
        return auto;
    }

    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", documento='" + documento + '\'' +
                ", auto=" + (auto != null ? auto.getPatente() : "Sin auto") +
                '}';
    }
}