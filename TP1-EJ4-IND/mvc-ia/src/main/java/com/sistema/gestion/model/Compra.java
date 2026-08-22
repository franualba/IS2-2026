package com.sistema.gestion.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * ============================================================================
 * MODELO (CAPA M de MVC) - Compra
 * ============================================================================
 * Atributos UML: id, fechaCompra, precioTotal.
 * Metodos UML: registrarCompra(), agregarDetalle(), anularCompra().
 *
 * Relaciones (UML):
 *  - Usuario (1...*) --> Compra : un Usuario puede realizar muchas Compras
 *    (asociacion simple, ManyToOne desde Compra hacia Usuario).
 *  - Compra (1) *composicion* (1...*) DetalleCompra : una Compra esta
 *    compuesta por 1 o mas DetalleCompra; si se elimina la Compra, se
 *    eliminan sus detalles (composicion => cascade ALL + orphanRemoval).
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"usuario", "detalles"})
@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_compra", nullable = false)
    private Date fechaCompra = new Date();

    @Column(name = "precio_total", nullable = false)
    private Double precioTotal = 0.0;

    /** Indica si la compra fue anulada (soporta el metodo anularCompra()). */
    @Column(nullable = false)
    private boolean anulada = false;

    /** Lado "muchos" de la relacion Usuario (1...*) --> Compra. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Composicion Compra (1) *--- (1...*) DetalleCompra (diamante relleno
     * del lado de Compra en el UML). cascade=ALL + orphanRemoval=true
     * expresan que el ciclo de vida de los DetalleCompra depende
     * completamente de su Compra contenedora.
     */
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detalles = new ArrayList<>();

    /**
     * +registrarCompra(): void
     * Inicializa/valida el encabezado de la compra antes de persistir.
     * La persistencia en si se dispara desde CompraServiceImpl.registrar().
     */
    public void registrarCompra() {
        if (this.fechaCompra == null) {
            this.fechaCompra = new Date();
        }
        this.anulada = false;
    }

    /**
     * +agregarDetalle(): void
     * Agrega un DetalleCompra a la compra, manteniendo consistente la
     * relacion bidireccional y recalculando el precio total.
     */
    public void agregarDetalle(DetalleCompra detalle) {
        detalle.setCompra(this);
        this.detalles.add(detalle);
        recalcularPrecioTotal();
    }

    /**
     * +anularCompra(): void
     * Marca la compra como anulada. La reposicion de stock (si corresponde)
     * se gestiona desde CompraServiceImpl.anular(), ya que requiere acceso
     * al InventarioService.
     */
    public void anularCompra() {
        this.anulada = true;
    }

    private void recalcularPrecioTotal() {
        this.precioTotal = this.detalles.stream()
                .mapToDouble(DetalleCompra::getSubtotal)
                .sum();
    }
}
