package com.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * ============================================================================
 * MODELO (CAPA M de MVC) - Inventario
 * ============================================================================
 * Representa un MOVIMIENTO de stock (entrada o salida) de un Producto en una
 * fecha determinada. Atributos UML: id, fecha, cantidad.
 * Metodo UML: registrarMovimiento().
 *
 * Relacion con Producto: lado "muchos" de la agregacion Producto(1)-Inventario(*).
 * Relacion con DetalleCompra: lado "1" de la asociacion DetalleCompra(*)-Inventario(1),
 * es decir, muchos detalles de compra pueden referenciar/impactar el mismo
 * producto en inventario a lo largo de distintas compras.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha = new Date();

    @Column(nullable = false)
    private int cantidad;

    /**
     * Tipo de movimiento: ENTRADA (compra/ingreso de stock) o SALIDA
     * (venta / consumo de stock, disparado por DetalleCompra.disminuirInventario()).
     * Agregado como mejora practica para poder calcular el stock actual
     * sumando/restando movimientos, sin modificar los atributos definidos
     * en el UML.
     */
    public enum TipoMovimiento { ENTRADA, SALIDA }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 10)
    private TipoMovimiento tipoMovimiento = TipoMovimiento.ENTRADA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /**
     * +registrarMovimiento(): void
     * Logica de negocio simple asociada al movimiento; la persistencia
     * concreta (guardar en la base) se realiza desde InventarioService.
     */
    public void registrarMovimiento() {
        if (this.fecha == null) {
            this.fecha = new Date();
        }
    }
}
