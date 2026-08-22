package com.sistema.gestion.service;

import com.sistema.gestion.model.Inventario;
import com.sistema.gestion.model.Producto;

import java.util.List;

/**
 * ============================================================================
 * SERVICIO - InventarioService
 * ============================================================================
 * Expone la operacion registrarMovimiento() definida en el UML para la
 * clase Inventario, tanto para entradas manuales de stock como para las
 * salidas generadas automaticamente por DetalleCompra.disminuirInventario().
 * ============================================================================
 */
public interface InventarioService {

    Inventario registrarMovimiento(Inventario movimiento);

    List<Inventario> listarPorProducto(Producto producto);

    List<Inventario> listarTodos();
}
