package com.sistema.gestion.repository;

import com.sistema.gestion.model.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ============================================================================
 * REPOSITORIO (ORM) - DetalleCompraRepository
 * ============================================================================
 */
@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
}
