package ar.edu.club.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Estado técnico transversal. Las entidades heredan quién/cuándo creó y modificó
 * cada registro sin duplicar columnas ni lógica en cada clase.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {
    @CreatedDate @Column(nullable = false, updatable = false)
    private Instant creadoEn;
    @LastModifiedDate @Column(nullable = false)
    private Instant actualizadoEn;
    @CreatedBy @Column(updatable = false)
    private String creadoPor;
    @LastModifiedBy
    private String actualizadoPor;

    public Instant getCreadoEn() { return creadoEn; }
    public Instant getActualizadoEn() { return actualizadoEn; }
    public String getCreadoPor() { return creadoPor; }
    public String getActualizadoPor() { return actualizadoPor; }
}
