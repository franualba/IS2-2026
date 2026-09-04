package com.is2.tp1ej6ind.repository;

import com.is2.tp1ej6ind.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad Profesor.
 *
 * Encapsula las consultas de persistencia y facilita la inyección en servicios.
 */
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    Optional<Profesor> findByEmail(String email);

    boolean existsByEmail(String email);
}
