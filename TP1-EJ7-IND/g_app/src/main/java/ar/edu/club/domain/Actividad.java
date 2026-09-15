package ar.edu.club.domain;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class Actividad extends AuditableEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(nullable = false) private String nombre;
    private LocalTime horario;
    private int cupos;
    private boolean eliminado;
    @ManyToOne(fetch = FetchType.LAZY) private Socio socio;
    protected Actividad() { }
    public Actividad(String nombre, int cupos) { this.nombre = nombre; this.cupos = cupos; }
    public String getNombre() { return nombre; }
    public int getCupos() { return cupos; }
}
