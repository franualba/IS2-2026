package com.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * ============================================================================
 * MODELO (CAPA M de MVC) - Persona
 * ============================================================================
 * Representa a una persona con datos personales, tal como se define en el
 * diagrama UML. Es la superclase de Usuario y Administrador (relacion de
 * herencia "Extends" del diagrama).
 *
 * Se modela como @MappedSuperclass (y no como @Entity con herencia JOINED/
 * SINGLE_TABLE) porque el diagrama UML no exige que "Persona" exista como
 * tabla propia consultable de forma independiente: solo aporta atributos y
 * comportamiento comun a Usuario y Administrador. Cada subclase persiste sus
 * propios atributos + los heredados en su propia tabla.
 *
 * Atributos (segun UML): nombre, apellido, documento, fechaDeNacimiento, correo.
 * Metodos (segun UML): Persona() (constructor) y getNombreCompleto().
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Persona {

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false, length = 100)
    private String apellido;

    /**
     * NOTA / MEJORA respecto del UML: el diagrama define "documento: int".
     * Se mantiene como Integer (wrapper) en lugar de int primitivo para poder
     * representar ausencia de valor (null) durante validaciones de formulario,
     * y se lo marca como campo unico ya que dos personas no deberian compartir
     * el mismo numero de documento.
     */
    @NotBlank(message = "El documento es obligatorio")
    @Column(nullable = false, length = 20, unique = true)
    private String documento;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaDeNacimiento;

    /**
     * Correo personal. Segun el enunciado, este campo se reutiliza ademas
     * como "usuario" (login) del sistema para la clase Usuario.
     */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido")
    @Column(nullable = false, length = 150)
    private String correo;

    /**
     * Metodo de conveniencia definido en el UML: +getNombreCompleto(): String
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }
}
