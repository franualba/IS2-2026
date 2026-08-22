package com.sistema.gestion.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * MODELO (CAPA M de MVC) - Producto
 * ============================================================================
 * Atributos UML: id, nombre, descripcion.
 * Metodos UML: registrarProducto(), editarProducto(), eliminarProducto().
 *
 * Relacion con Inventario (UML): agregacion (diamante hueco del lado de
 * Producto) *...1, interpretada como "un Producto tiene muchos registros de
 * Inventario (movimientos de stock) a lo largo del tiempo, y cada registro
 * de Inventario pertenece a exactamente un Producto". Se implementa como
 * OneToMany desde Producto hacia Inventario.
 *
 * Los metodos registrarProducto()/editarProducto()/eliminarProducto() del
 * UML son operaciones de alta/edicion/baja; se implementan como logica de
 * Service (ProductoService), ya que requieren el repositorio para persistir
 * cambios. Aqui se documentan como comentario para respetar el diagrama.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "inventarios")
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    /**
     * Lado "1" de la agregacion Producto (1) -- (*) Inventario.
     * mappedBy = "producto" indica que la clave foranea vive en Inventario.
     */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Inventario> inventarios = new ArrayList<>();

    // -- registrarProducto(): void  -> ver ProductoServiceImpl.registrar()
    // -- editarProducto(): void     -> ver ProductoServiceImpl.editar()
    // -- eliminarProducto(): void   -> ver ProductoServiceImpl.eliminar()
}
