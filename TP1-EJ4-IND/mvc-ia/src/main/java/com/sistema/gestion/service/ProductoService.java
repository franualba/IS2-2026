package com.sistema.gestion.service;

import com.sistema.gestion.model.Producto;

import java.util.List;

/**
 * ============================================================================
 * SERVICIO - ProductoService
 * ============================================================================
 * Expone las operaciones registrarProducto() / editarProducto() /
 * eliminarProducto() definidas en el UML.
 * ============================================================================
 */
public interface ProductoService {

    Producto registrarProducto(Producto producto);

    Producto editarProducto(Long id, Producto datosActualizados);

    void eliminarProducto(Long id);

    Producto buscarPorId(Long id);

    List<Producto> listarTodos();

    /** Calcula el stock actual (entradas - salidas) de un producto. */
    int calcularStockActual(Long productoId);
}
