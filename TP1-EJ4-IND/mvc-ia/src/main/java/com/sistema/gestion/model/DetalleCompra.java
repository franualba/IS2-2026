package com.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ============================================================================
 * MODELO (CAPA M de MVC) - DetalleCompra
 * ============================================================================
 * Atributo UML: id.
 * Metodo UML: disminuirInventario().
 * Relaciones UML: Compra (1) *--- (1...*) DetalleCompra [composicion];
 *                 DetalleCompra (*...1) --> Inventario [asociacion].
 *
 * -----------------------------------------------------------------------
 * CORRECCION / MEJORA respecto del UML (documentada, error detectado):
 * -----------------------------------------------------------------------
 * El diagrama original define la clase DetalleCompra con el UNICO atributo
 * "id: int". Esto es un error de modelado: un detalle de compra necesita,
 * como minimo, saber A QUE PRODUCTO se refiere y QUE CANTIDAD se compro,
 * de lo contrario el metodo disminuirInventario() no tendria forma de saber
 * cuanto stock descontar. Se agregaron entonces los atributos:
 *      - cantidad: int
 *      - precioUnitario: Double
 *      - producto: Producto (referencia, necesaria para disminuirInventario)
 * y el atributo derivado getSubtotal() (cantidad * precioUnitario), usado
 * por Compra.agregarDetalle() para recalcular el precioTotal de la Compra.
 * La asociacion "DetalleCompra (*...1) --> Inventario" del diagrama original
 * se conserva tal cual, ya que cada vez que se registra un detalle se genera
 * un movimiento de SALIDA en Inventario (ver disminuirInventario()).
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"compra"})
@Entity
@Table(name = "detalle_compra")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario = 0.0;

    /** Lado "muchos" de la composicion Compra (1) *--- (*) DetalleCompra. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    /** Producto sobre el cual se realiza este detalle de compra. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /**
     * Referencia opcional al ultimo movimiento de Inventario generado por
     * este detalle (asociacion DetalleCompra *...1 --> Inventario del UML).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id")
    private Inventario inventario;

    public double getSubtotal() {
        return this.cantidad * this.precioUnitario;
    }

    /**
     * +disminuirInventario(): void
     * Genera (y devuelve) un nuevo movimiento de SALIDA de Inventario por la
     * cantidad de este detalle, para el producto asociado. La persistencia
     * del movimiento se realiza desde CompraServiceImpl, que es quien tiene
     * acceso a InventarioService/InventarioRepository.
     */
    public Inventario disminuirInventario() {
        Inventario movimiento = new Inventario();
        movimiento.setProducto(this.producto);
        movimiento.setCantidad(this.cantidad);
        movimiento.setTipoMovimiento(Inventario.TipoMovimiento.SALIDA);
        movimiento.registrarMovimiento();
        this.inventario = movimiento;
        return movimiento;
    }
}
