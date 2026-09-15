package ar.edu.club.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

/** Persona base del modelo. La imagen se separa para permitir almacenamiento/retención independiente. */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona extends AuditableEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(nullable = false) private String nombre;
    @Column(nullable = false) private String apellido;
    private LocalDate fechaNacimiento;
    private boolean eliminado;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) private Imagen imagen;
    protected Persona() { }
    public Persona(String nombre, String apellido) { this.nombre = nombre; this.apellido = apellido; }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public boolean isEliminado() { return eliminado; }
    public Imagen getImagen() { return imagen; }
    public void setImagen(Imagen imagen) { this.imagen = imagen; }
}
