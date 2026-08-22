package com.sistema.gestion.repository;

import com.sistema.gestion.model.Inventario;
import com.sistema.gestion.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================================
 * REPOSITORIO (ORM) - InventarioRepository
 * ============================================================================
 * Ademas del CRUD basico, se agrega un query method para listar todos los
 * movimientos de un producto determinado, util para calcular el stock
 * actual (suma de ENTRADAS - suma de SALIDAS).
 * ============================================================================
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByProductoOrderByFechaDesc(Producto producto);
}
