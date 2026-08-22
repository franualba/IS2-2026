package com.sistema.gestion.service.impl;

import com.sistema.gestion.model.Inventario;
import com.sistema.gestion.model.Producto;
import com.sistema.gestion.repository.InventarioRepository;
import com.sistema.gestion.repository.ProductoRepository;
import com.sistema.gestion.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * ============================================================================
 * SERVICIO (IMPLEMENTACION) - ProductoServiceImpl
 * ============================================================================
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                                InventarioRepository inventarioRepository) {
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    /** +registrarProducto(): void (UML) */
    @Override
    @Transactional
    public Producto registrarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /** +editarProducto(): void (UML) */
    @Override
    @Transactional
    public Producto editarProducto(Long id, Producto datosActualizados) {
        Producto existente = buscarPorId(id);
        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        return productoRepository.save(existente);
    }

    /** +eliminarProducto(): void (UML) */
    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe el producto con id " + id));
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public int calcularStockActual(Long productoId) {
        Producto producto = buscarPorId(productoId);
        List<Inventario> movimientos = inventarioRepository.findByProductoOrderByFechaDesc(producto);
        int stock = 0;
        for (Inventario mov : movimientos) {
            stock += (mov.getTipoMovimiento() == Inventario.TipoMovimiento.SALIDA)
                    ? -mov.getCantidad()
                    : mov.getCantidad();
        }
        return stock;
    }
}
