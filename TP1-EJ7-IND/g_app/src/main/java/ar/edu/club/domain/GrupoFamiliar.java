package ar.edu.club.domain;

import jakarta.persistence.*;
import java.util.*;

/** Agregado familiar: concentra socios y pagos de cuota, evitando cobrar cuota por persona. */
@Entity
public class GrupoFamiliar extends AuditableEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(nullable = false) private String denominacion;
    @OneToMany(mappedBy = "grupoFamiliar") private List<Socio> familiares = new ArrayList<>();
    @OneToMany(mappedBy = "grupoFamiliar", cascade = CascadeType.ALL, orphanRemoval = true) private List<PagoCuota> pagos = new ArrayList<>();
    protected GrupoFamiliar() { }
    public GrupoFamiliar(String denominacion) { this.denominacion = denominacion; }
    public String getId() { return id; }
    public String getDenominacion() { return denominacion; }
    public List<Socio> getFamiliares() { return familiares; }
    public List<PagoCuota> getPagos() { return pagos; }
    public void agregarSocio(Socio socio) { familiares.add(socio); socio.asignarGrupo(this); }
}
