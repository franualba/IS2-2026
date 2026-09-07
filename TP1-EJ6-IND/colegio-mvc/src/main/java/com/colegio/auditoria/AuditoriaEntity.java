package com.colegio.auditoria;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * SUPERCLASE DE AUDITORÍA.
 *
 * Todas las entidades del sistema heredan de esta clase para registrar
 * automáticamente: quién creó/modificó el registro y cuándo.
 *
 * @MappedSuperclass: los campos se heredan y se mapean como columnas en CADA tabla
 *                    hija (profesores, alumnos, materias...), sin crear tabla propia.
 * @EntityListeners(AuditingEntityListener.class): "escucha" los eventos de JPA
 *                    (@PrePersist / @PreUpdate) y rellena los campos de auditoría.
 * @CreatedDate / @LastModifiedDate: fecha/hora de creación y última modificación.
 * @CreatedBy / @LastModifiedBy: usuario autenticado; lo provee el Bean
 *                    AuditorAware<String> definido en AuditoriaConfig (toma el
 *                    nombre de usuario del SecurityContext).
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditoriaEntity {

    @CreatedDate
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @CreatedBy
    @Column(name = "creado_por", updatable = false, length = 100)
    private String creadoPor;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @LastModifiedBy
    @Column(name = "modificado_por", length = 100)
    private String modificadoPor;
}