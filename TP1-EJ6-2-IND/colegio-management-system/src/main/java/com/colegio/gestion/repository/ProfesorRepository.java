package com.colegio.gestion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.colegio.gestion.entity.Profesor;

/**
 * REPOSITORIO: ProfesorRepository
 * 
 * DESCRIPCIÓN:
 * Interfaz para operaciones CRUD con la entidad Profesor.
 * Extiende JpaRepository que proporciona métodos básicos de persistencia.
 * 
 * MÉTODOS DERIVADOS DE NOMBRE:
 * Spring Data JPA genera consultas automáticamente basadas en el nombre del método.
 * Ejemplo: findByEmail -> SELECT * FROM profesores WHERE email = ?
 * 
 * SEGURIDAD:
 * El método findByEmail se usa en CustomUserDetailsService para autenticación.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {

    /**
     * MÉTODO: findByEmail
     * DESCRIPCIÓN: Busca un profesor por su email (username para login).
     * Usado por Spring Security para autenticación.
     * 
     * @param email Correo electrónico del profesor
     * @return Optional<Profesor> Contiene el profesor si existe
     */
    Optional<Profesor> findByEmail(String email);

    /**
     * MÉTODO: findByEliminadoFalse
     * DESCRIPCIÓN: Retorna todos los profesores activos (no eliminados).
     * Implementa soft filter para queries de listados.
     * 
     * @return List<Profesor> Lista de profesores activos
     */
    List<Profesor> findByEliminadoFalse();

    /**
     * MÉTODO: existsByEmail
     * DESCRIPCIÓN: Verifica si existe un profesor con ese email.
     * Útil para validación de unicidad antes de guardar.
     * 
     * @param email Correo a verificar
     * @return boolean true si existe, false si no
     */
    boolean existsByEmail(String email);

    /**
     * MÉTODO: countByEliminadoFalse
     * DESCRIPCIÓN: Cuenta profesores activos.
     * 
     * @return long Número de profesores activos
     */
    long countByEliminadoFalse();

    /**
     * MÉTODO: buscarPorNombreOAPELLIDO (JPQL)
     * DESCRIPCIÓN: Búsqueda flexible por nombre o apellido.
     * 
     * @param termino Término de búsqueda
     * @return List<Profesor> Profesores que coinciden
     */
    @Query("SELECT p FROM Profesor p WHERE p.eliminado = false AND " +
           "(LOWER(p.nombre) LIKE LOWER(:termino) OR LOWER(p.apellido) LIKE LOWER(:termino))")
    List<Profesor> buscarPorNombreOApellido(@Param("termino") String termino);
}
