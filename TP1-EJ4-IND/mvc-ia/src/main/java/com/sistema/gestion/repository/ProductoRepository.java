package com.sistema.gestion.repository;

import com.sistema.gestion.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ============================================================================
 * REPOSITORIO (ORM) - ProductoRepository
 * ============================================================================
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
