package com.colegio.gestion.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CLASE: Persona (Entidad Base - Superclase)
 * 
 * DESCRIPCIÓN:
 * Esta es la clase base abstracta del sistema que representa a una persona genérica.
 * Utiliza el patrón de herencia de JPA (@Inheritance) para que Profesor y Alumno
 * hereden sus atributos y comportamientos comunes.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @MappedSuperclass: Indica que esta clase no es una entidad JPA per se,
 *   pero sus campos se mapearán a las tablas de las subclases.
 * - @Inheritance(strategy = InheritanceType.JOINED): Estrategia de herencia
 *   donde cada clase tiene su propia tabla y se unen mediante foreign keys.
 * - @EntityListeners(AuditingEntityListener.class): Habilita la auditoría
 *   automática de campos como fecha de creación, modificación, usuario, etc.
 * - @Data (Lombok): Genera automáticamente getters, setters, toString, equals, hashCode.
 * - @NoArgsConstructor: Constructor vacío requerido por JPA.
 * 
 * AUDITORÍA:
 * Los campos anotados con @CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy
 * se llenan automáticamente gracias a AuditingEntityListener cuando está habilitada
 * la auditoría en la configuración de JPA.
 * 
 * RELACIONES:
 * - Herencia: Profesor extends Persona
 * - Herencia: Alumno extends Persona
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Persona implements Serializable {

    /**
     * serialVersionUID: Identificador único para serialización/deserialización.
     * Requerido cuando una clase implementa Serializable.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: nombre
     * TIPO: String
     * DESCRIPCIÓN: Nombre de la persona (profesor o alumno).
     * ANOTACIONES:
     * - @Column(nullable = false, length = 100): No nulo, máximo 100 caracteres.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * ATRIBUTO: apellido
     * TIPO: String
     * DESCRIPCIÓN: Apellido de la persona (profesor o alumno).
     * ANOTACIONES:
     * - @Column(nullable = false, length = 100): No nulo, máximo 100 caracteres.
     */
    @Column(nullable = false, length = 100)
    private String apellido;

    // =========================================================================
    // CAMPOS DE AUDITORÍA (automáticamente gestionados por Spring Data JPA)
    // =========================================================================

    /**
     * ATRIBUTO: fechaCreacion
     * TIPO: LocalDateTime
     * DESCRIPCIÓN: Fecha y hora cuando se creó el registro.
     * ANOTACIONES:
     * - @CreatedDate: Se llena automáticamente al persistir la entidad.
     * - @Column(updatable = false): No se puede modificar después de la creación.
     */
    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaCreacion;

    /**
     * ATRIBUTO: fechaModificacion
     * TIPO: LocalDateTime
     * DESCRIPCIÓN: Fecha y hora de la última modificación del registro.
     * ANOTACIONES:
     * - @LastModifiedDate: Se actualiza automáticamente en cada update.
     */
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaModificacion;

    /**
     * ATRIBUTO: creadoPor
     * TIPO: String
     * DESCRIPCIÓN: Usuario que creó el registro (obtenido del SecurityContext).
     * ANOTACIONES:
     * - @CreatedBy: Se llena con el username del usuario autenticado.
     */
    @CreatedBy
    @Column(updatable = false, length = 50)
    private String creadoPor;

    /**
     * ATRIBUTO: modificadoPor
     * TIPO: String
     * DESCRIPCIÓN: Usuario que realizó la última modificación.
     * ANOTACIONES:
     * - @LastModifiedBy: Se actualiza con el username actual.
     */
    @LastModifiedBy
    @Column(length = 50)
    private String modificadoPor;

    /**
     * CONSTRUCTOR: Persona
     * DESCRIPCIÓN: Constructor con parámetros para inicializar nombre y apellido.
     * Las subclases (Profesor, Alumno) pueden llamar a este constructor con super().
     * 
     * @param nombre Nombre de la persona
     * @param apellido Apellido de la persona
     */
    public Persona(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    /**
     * MÉTODO: getNombreCompleto
     * DESCRIPCIÓN: Retorna el nombre completo concatenando apellido y nombre.
     * Útil para mostrar en vistas o reportes.
     * 
     * @return String con formato "Apellido, Nombre"
     */
    public String getNombreCompleto() {
        return this.apellido + ", " + this.nombre;
    }
}
