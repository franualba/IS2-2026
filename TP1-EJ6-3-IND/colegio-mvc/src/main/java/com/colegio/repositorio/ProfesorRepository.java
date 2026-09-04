package com.colegio.repositorio;

import com.colegio.modelo.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORIO de Profesor. Extender JpaRepository otorga CRUD completo
 * (save, findById, findAll, delete...) sin escribir SQL.
 *
 * SEGURIDAD: findByUsuario es la consulta que usa Spring Security para
 * autenticar. Los métodos con "ByEliminadoFalse" implementan el filtrado por
 * eliminación lógica a nivel de consulta.
 */
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    /** Busca por correo-usuario (login). */
    Optional<Profesor> findByUsuario(String usuario);

    /** Permite validar correos duplicados en el registro. */
    boolean existsByUsuario(String usuario);

    /** Lista solo profesores no eliminados lógicamente. */
    List<Profesor> findByEliminadoFalse();
}