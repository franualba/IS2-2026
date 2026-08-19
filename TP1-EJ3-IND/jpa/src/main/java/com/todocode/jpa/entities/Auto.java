package com.todocode.jpa.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "autos")
public class Auto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auto")
    private Long id;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "patente", length = 10, unique = true, nullable = false)
    private String patente;

    // Relación UNO A UNO: Un auto pertenece a UNA persona
    @OneToOne
    @JoinColumn(name = "id_persona_fk", referencedColumnName = "id_persona")
    private Persona persona;

    public Auto() {
    }

    public Auto(String marca, String modelo, String patente) {
        this.marca = marca;
        this.modelo = modelo;
        this.patente = patente;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public String toString() {
        return "Auto{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", patente='" + patente + '\'' +
                '}';
    }
}