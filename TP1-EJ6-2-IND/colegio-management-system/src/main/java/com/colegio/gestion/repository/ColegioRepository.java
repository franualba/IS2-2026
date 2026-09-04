package com.colegio.gestion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.colegio.gestion.entity.Colegio;

/**
 * REPOSITORIO: ColegioRepository
 * 
 * DESCRIPCIÓN:
 * Interfaz que extiende JpaRepository para operaciones CRUD con la entidad Colegio.
 * Spring Data JPA genera automáticamente la implementación en tiempo de ejecución.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Repository: Marca esta interfaz como componente de persistencia.
 *   Permite detección automática por component scanning y traducción de excepciones.
 * - JpaRepository<T, ID>: Proporciona métodos CRUD básicos:
 *   - save(T entity): Guarda o actualiza una entidad.
 *   - findById(ID id): Busca por ID, retorna Optional.
 *   - findAll(): Lista todas las entidades.
 *   - deleteById(ID id): Elimina por ID.
 *   - count(): Cuenta registros.
 * 
 * MÉTODOS CUSTOM:
 * Spring Data JPA permite definir métodos personalizados basados en nombres:
 * - findByNombre(String nombre): Busca colegios por nombre exacto.
 * - findByEliminadoFalse(): Filtra solo colegios activos (no eliminados).
 * 
 * CONSULTAS JPQL:
 * Se pueden definir consultas personalizadas con @Query usando JPQL o SQL nativo.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Repository
public interface ColegioRepository extends JpaRepository<Colegio, Integer> {

    /**
     * MÉTODO: findByNombre
     * DESCRIPCIÓN: Busca un colegio por nombre exacto.
     * Spring Data JPA genera automáticamente la consulta SQL.
     * 
     * @param nombre Nombre del colegio a buscar
     * @return Optional<Colegio> Contiene el colegio si existe, vacío si no
     */
    Optional<Colegio> findByNombre(String nombre);

    /**
     * MÉTODO: findByEliminadoFalse
     * DESCRIPCIÓN: Retorna todos los colegios que no están eliminados (activos).
     * Implementa soft filter para no mostrar registros lógicamente eliminados.
     * 
     * @return List<Colegio> Lista de colegios activos
     */
    List<Colegio> findByEliminadoFalse();

    /**
     * MÉTODO: findByNombreContainingIgnoreCase
     * DESCRIPCIÓN: Búsqueda parcial de colegios por nombre (case-insensitive).
     * Útil para autocomplete o búsquedas tipo "contains".
     * 
     * @param nombre Fragmento del nombre a buscar
     * @return List<Colegio> Lista de colegios que coinciden parcialmente
     */
    List<Colegio> findByNombreContainingIgnoreCase(String nombre);

    /**
     * MÉTODO: countByEliminadoFalse
     * DESCRIPCIÓN: Cuenta la cantidad total de colegios activos.
     * 
     * @return long Número de colegios activos
     */
    long countByEliminadoFalse();

    /**
     * MÉTODO: customBuscarColegiosConGrados (Consulta JPQL)
     * DESCRIPCIÓN: Consulta personalizada que retorna colegios con sus grados.
     * Usa JOIN FETCH para cargar la relación en una sola query (evita N+1).
     * 
     * @return List<Colegio> Colegios con grados cargados eager
     */
    @Query("SELECT DISTINCT c FROM Colegio c LEFT JOIN FETCH c.grados WHERE c.eliminado = false")
    List<Colegio> buscarColegiosConGrados();

    /**
     * MÉTODO: customBuscarPorNombreOPorDireccion (Consulta JPQL)
     * DESCRIPCIÓN: Búsqueda flexible por nombre O dirección.
     * Usa parámetros nombrados (:parametro) para mayor claridad.
     * 
     * @param termino Término de búsqueda (nombre o dirección)
     * @return List<Colegio> Colegios que coinciden
     */
    @Query("SELECT c FROM Colegio c WHERE c.eliminado = false AND " +
           "(LOWER(c.nombre) LIKE LOWER(:termino) OR LOWER(c.direccion) LIKE LOWER(:termino))")
    List<Colegio> buscarPorNombreOPorDireccion(@Param("termino") String termino);
}
