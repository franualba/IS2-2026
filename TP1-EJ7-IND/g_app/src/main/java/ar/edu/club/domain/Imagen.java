package ar.edu.club.domain;

import jakarta.persistence.*;

/** Imagen facial como entidad propia; el contenido binario no se expone directamente en DTOs. */
@Entity
public class Imagen extends AuditableEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    private String nombre;
    private String mime;
    @Lob @Basic(fetch = FetchType.LAZY) private byte[] contenido;
    private boolean eliminado;
    protected Imagen() { }
    public Imagen(String nombre, String mime, byte[] contenido) { this.nombre = nombre; this.mime = mime; this.contenido = contenido; }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getMime() { return mime; }
    public byte[] getContenido() { return contenido; }
}
