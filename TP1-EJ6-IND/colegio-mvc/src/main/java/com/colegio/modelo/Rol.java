package com.colegio.modelo;

import com.colegio.auditoria.AuditoriaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * ENTIDAD NUEVA del rediseño: roles de seguridad (ROLE_PROFESOR, ROLE_ADMIN).
 * Se relaciona N:M con Profesor mediante la tabla intermedia "profesor_roles".
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Rol extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // columna serial/bigserial en PostgreSQL
    private Long idRol;

    /** unique=true crea restricción UNIQUE a nivel de BD. */
    @Column(unique = true, nullable = false, length = 50)
    private String nombre; // ej: "ROLE_PROFESOR"
}