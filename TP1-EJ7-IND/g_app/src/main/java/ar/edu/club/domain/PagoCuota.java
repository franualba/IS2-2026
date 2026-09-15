package ar.edu.club.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Pago asociado a la familia y a un período; la unicidad del período evita doble cobro. */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"grupo_familiar_id", "periodo"}))
public class PagoCuota extends AuditableEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private GrupoFamiliar grupoFamiliar;
    @Column(nullable = false) private String periodo;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal importe;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private MedioPago medioPago;
    @Column(nullable = false) private LocalDate fechaPago;
    protected PagoCuota() { }
    public PagoCuota(GrupoFamiliar grupo, String periodo, BigDecimal importe, MedioPago medio) { this.grupoFamiliar = grupo; this.periodo = periodo; this.importe = importe; this.medioPago = medio; this.fechaPago = LocalDate.now(); }
    public String getId() { return id; }
    public String getPeriodo() { return periodo; }
    public BigDecimal getImporte() { return importe; }
    public MedioPago getMedioPago() { return medioPago; }
    public LocalDate getFechaPago() { return fechaPago; }
}
