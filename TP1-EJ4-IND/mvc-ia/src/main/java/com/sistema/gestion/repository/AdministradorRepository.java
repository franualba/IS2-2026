package com.sistema.gestion.repository;

import com.sistema.gestion.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================================
 * REPOSITORIO (ORM) - AdministradorRepository
 * ============================================================================
 * Acceso a datos de la entidad Administrador, analogo a UsuarioRepository.
 * ============================================================================
 */
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    Optional<Administrador> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}
