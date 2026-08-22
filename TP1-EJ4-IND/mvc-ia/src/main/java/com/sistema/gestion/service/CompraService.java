package com.sistema.gestion.service;

import com.sistema.gestion.model.Compra;
import com.sistema.gestion.model.Usuario;

import java.util.List;

/**
 * ============================================================================
 * SERVICIO - CompraService
 * ============================================================================
 * Expone registrarCompra() / agregarDetalle() / anularCompra() del UML,
 * orquestando ademas la actualizacion de Inventario (disminuirInventario())
 * por cada DetalleCompra agregado.
 * ============================================================================
 */
public interface CompraService {

    /**
     * Registra una compra completa junto con sus detalles, descontando
     * stock de Inventario por cada detalle (Compra.registrarCompra() +
     * Compra.agregarDetalle() + DetalleCompra.disminuirInventario()).
     */
    Compra registrarCompra(Compra compra);

    void anularCompra(Long compraId);

    List<Compra> listarPorUsuario(Usuario usuario);

    List<Compra> listarTodas();

    Compra buscarPorId(Long id);
}
