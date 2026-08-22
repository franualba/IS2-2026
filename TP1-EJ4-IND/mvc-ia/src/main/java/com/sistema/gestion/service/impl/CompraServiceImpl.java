package com.sistema.gestion.service.impl;

import com.sistema.gestion.model.Compra;
import com.sistema.gestion.model.DetalleCompra;
import com.sistema.gestion.model.Inventario;
import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.repository.CompraRepository;
import com.sistema.gestion.repository.InventarioRepository;
import com.sistema.gestion.service.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * ============================================================================
 * SERVICIO (IMPLEMENTACION) - CompraServiceImpl
 * ============================================================================
 * Orquesta el flujo completo: registrarCompra() -> agregarDetalle() (por
 * cada item) -> disminuirInventario() (por cada detalle), persistiendo al
 * final la Compra junto con sus DetalleCompra (gracias al cascade=ALL
 * definido en Compra.detalles) y cada movimiento de Inventario generado.
 * ============================================================================
 */
@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final InventarioRepository inventarioRepository;

    public CompraServiceImpl(CompraRepository compraRepository,
                              InventarioRepository inventarioRepository) {
        this.compraRepository = compraRepository;
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    @Transactional
    public Compra registrarCompra(Compra compra) {
        // +registrarCompra(): void (UML) -> inicializa fecha/estado
        compra.registrarCompra();

        // Se toma una copia de los detalles recibidos (por ejemplo, armados
        // desde el formulario del Controller) y se vuelven a agregar usando
        // Compra.agregarDetalle(), para respetar exactamente el metodo
        // definido en el UML y mantener consistente la relacion bidireccional.
        List<DetalleCompra> detallesRecibidos = List.copyOf(compra.getDetalles());
        compra.getDetalles().clear();

        for (DetalleCompra detalle : detallesRecibidos) {
            // +agregarDetalle(): void (UML)
            compra.agregarDetalle(detalle);

            // +disminuirInventario(): void (UML) -> genera el movimiento de
            // salida de stock correspondiente a este detalle.
            Inventario movimientoSalida = detalle.disminuirInventario();
            inventarioRepository.save(movimientoSalida);
        }

        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public void anularCompra(Long compraId) {
        Compra compra = buscarPorId(compraId);
        // +anularCompra(): void (UML)
        compra.anularCompra();

        // Mejora: al anular la compra, se repone el stock descontado
        // generando un movimiento de ENTRADA por cada detalle, para
        // mantener la consistencia del inventario.
        for (DetalleCompra detalle : compra.getDetalles()) {
            Inventario reposicion = new Inventario();
            reposicion.setProducto(detalle.getProducto());
            reposicion.setCantidad(detalle.getCantidad());
            reposicion.setTipoMovimiento(Inventario.TipoMovimiento.ENTRADA);
            reposicion.registrarMovimiento();
            inventarioRepository.save(reposicion);
        }

        compraRepository.save(compra);
    }

    @Override
    public List<Compra> listarPorUsuario(Usuario usuario) {
        return compraRepository.findByUsuarioOrderByFechaCompraDesc(usuario);
    }

    @Override
    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }

    @Override
    public Compra buscarPorId(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe la compra con id " + id));
    }
}
