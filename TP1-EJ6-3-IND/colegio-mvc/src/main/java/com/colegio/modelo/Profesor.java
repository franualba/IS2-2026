package com.colegio.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * ENTIDAD PROFESOR (hereda de Persona).
 *
 * REDISEÑO: incorpora las credenciales de acceso:
 *  - usuario:   correo personal del docente (username de Spring Security).
 *  - password:  SIEMPRE almacenado como HASH BCrypt (nunca texto plano).
 *  - habilitado: permite desactivar cuentas sin borrarlas.
 *  - roles:     relación N:M con Rol (autorización).
 *
 * Los métodos del diagrama (registrarProfesor, editarProfesor, eliminarProfesor)
 * viven en la CAPA DE SERVICIO (ProfesorServicio) respetando MVC.
 */
@Entity
@Table(name = "profesores")
@Getter
@Setter
public class Profesor extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfesor;

    private String especialidad;

    /** Eliminación LÓGICA: el registro no se borra de la BD, se marca. */
    private boolean eliminado = false;

    /** Correo personal = usuario de acceso. unique garantiza un solo usuario por correo. */
    @Column(unique = true, nullable = false, length = 100)
    private String usuario;

    /** Hash BCrypt de la contraseña. nullable=false obliga su asignación al registrar. */
    @Column(nullable = false)
    private String password;

    private boolean habilitado = true;

    /**
     * SEGURIDAD: FetchType.EAGER es intencional aquí. Spring Security necesita
     * los roles disponibles inmediatamente después de cargar el usuario, sin
     * riesgo de LazyInitializationException fuera de la transacción.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profesor_roles",
            joinColumns = @JoinColumn(name = "profesor_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    /** Agregación del diagrama: Colegio 1 ── * Profesor. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id")
    private Colegio colegio;
}