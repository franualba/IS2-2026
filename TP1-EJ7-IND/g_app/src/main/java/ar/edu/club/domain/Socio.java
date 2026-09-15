package ar.edu.club.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

/** Socio extiende Persona y mantiene la composición con una única familia titular. */
@Entity
public class Socio extends Persona {
    private LocalDate fechaAlta;
    @Enumerated(EnumType.STRING) private EstadoSocio estado = EstadoSocio.ACTIVO;
    @ManyToOne(fetch = FetchType.LAZY) private GrupoFamiliar grupoFamiliar;
    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true) private List<Actividad> actividades = new ArrayList<>();
    protected Socio() { }
    public Socio(String nombre, String apellido) { super(nombre, apellido); this.fechaAlta = LocalDate.now(); }
    public EstadoSocio getEstado() { return estado; }
    public GrupoFamiliar getGrupoFamiliar() { return grupoFamiliar; }
    public void asignarGrupo(GrupoFamiliar grupo) { this.grupoFamiliar = grupo; }
}
