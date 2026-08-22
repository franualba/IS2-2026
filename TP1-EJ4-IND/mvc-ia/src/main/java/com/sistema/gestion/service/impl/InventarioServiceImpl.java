package com.sistema.gestion.service.impl;

import com.sistema.gestion.model.Inventario;
import com.sistema.gestion.model.Producto;
import com.sistema.gestion.repository.InventarioRepository;
import com.sistema.gestion.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================================
 * SERVICIO (IMPLEMENTACION) - InventarioServiceImpl
 * ============================================================================
 */
@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    /** +registrarMovimiento(): void (UML) */
    @Override
    @Transactional
    public Inventario registrarMovimiento(Inventario movimiento) {
        movimiento.registrarMovimiento();
        return inventarioRepository.save(movimiento);
    }

    @Override
    public List<Inventario> listarPorProducto(Producto producto) {
        return inventarioRepository.findByProductoOrderByFechaDesc(producto);
    }

    @Override
    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }
}
