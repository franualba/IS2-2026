package com.colegio.gestion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.colegio.gestion.entity.Alumno;

/**
 * REPOSITORIO: AlumnoRepository
 * 
 * DESCRIPCIÓN:
 * Interfaz para operaciones CRUD con la entidad Alumno.
 * Proporciona métodos para búsqueda y filtrado de alumnos.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    /**
     * MÉTODO: findByEliminadoFalse
     * DESCRIPCIÓN: Retorna todos los alumnos activos (no eliminados).
     * 
     * @return List<Alumno> Lista de alumnos activos
     */
    List<Alumno> findByEliminadoFalse();

    /**
     * MÉTODO: existsByLegajo
     * DESCRIPCIÓN: Verifica si existe un alumno con ese legajo.
     * 
     * @param legajo Número de legajo a verificar
     * @return boolean true si existe
     */
    boolean existsByLegajo(String legajo);

    /**
     * MÉTODO: countByEliminadoFalse
     * DESCRIPCIÓN: Cuenta alumnos activos.
     * 
     * @return long Número de alumnos activos
     */
    long countByEliminadoFalse();

    /**
     * MÉTODO: buscarPorNombreOApellido (JPQL)
     * DESCRIPCIÓN: Búsqueda flexible por nombre o apellido.
     * 
     * @return List<Alumno> Alumnos que coinciden
     */
    @Query("SELECT a FROM Alumno a WHERE a.eliminado = false AND " +
           "(LOWER(a.nombre) LIKE LOWER(:termino) OR LOWER(a.apellido) LIKE LOWER(:termino))")
    List<Alumno> buscarPorNombreOApellido(@Param("termino") String termino);
}
