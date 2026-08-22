package com.sistema.gestion.repository;

import com.sistema.gestion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================================
 * REPOSITORIO (ORM) - UsuarioRepository
 * ============================================================================
 * Interfaz de acceso a datos para la entidad Usuario, parte de la capa de
 * persistencia (Modelo). Extiende JpaRepository, que provee automaticamente
 * (via Spring Data JPA / Hibernate) las operaciones CRUD basicas (save,
 * findById, findAll, deleteById, etc.) sin necesidad de escribir SQL.
 *
 * Los metodos "findByCorreo" y "existsByCorreo" son "query methods":
 * Spring Data JPA genera la consulta SQL automaticamente a partir del
 * nombre del metodo (equivalente a
 *   SELECT * FROM usuarios WHERE correo = ? ).
 * Recordar que segun el enunciado, el correo personal se usa como
 * usuario (login) del sistema.
 * ============================================================================
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByDocumento(String documento);
}
