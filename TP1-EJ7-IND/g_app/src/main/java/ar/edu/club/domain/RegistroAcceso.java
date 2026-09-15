package ar.edu.club.domain;

import jakarta.persistence.*;
import java.time.*;

/** Un registro por persona y jornada; las horas se completan mediante casos de uso separados. */
@Entity
public class RegistroAcceso extends AuditableEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private Persona persona;
    @Column(nullable = false) private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    protected RegistroAcceso() { }
    public RegistroAcceso(Persona persona) { this.persona = persona; this.fecha = LocalDate.now(); }
    public void registrarEntrada() { horaEntrada = LocalTime.now(); }
    public void registrarSalida() { horaSalida = LocalTime.now(); }
    public Duration obtenerDuracion() { return horaEntrada != null && horaSalida != null ? Duration.between(horaEntrada, horaSalida) : Duration.ZERO; }
}
