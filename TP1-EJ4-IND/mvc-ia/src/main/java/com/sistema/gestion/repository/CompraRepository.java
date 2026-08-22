package com.sistema.gestion.repository;

import com.sistema.gestion.model.Compra;
import com.sistema.gestion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================================
 * REPOSITORIO (ORM) - CompraRepository
 * ============================================================================
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByUsuarioOrderByFechaCompraDesc(Usuario usuario);
}
