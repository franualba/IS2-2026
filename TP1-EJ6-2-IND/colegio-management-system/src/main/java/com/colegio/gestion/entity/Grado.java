package com.colegio.gestion.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

/**
 * CLASE: Grado (Entidad)
 * 
 * DESCRIPCIÓN:
 * Representa un nivel educativo dentro del colegio (ej: Primario, Secundario).
 * Cada grado contiene múltiples aulas (divisiones/secciones).
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Entity: Marca como entidad JPA persistente.
 * - @Table(name = "grados"): Nombre explícito de tabla en PostgreSQL.
 * - @Data, @NoArgsConstructor (Lombok): Generación automática de código.
 * 
 * RELACIONES DEL DIAGRAMA UML:
 * - Colegio 1 -- * Grado (Agregación): Un colegio tiene múltiples grados.
 * - Grado 1 -- 1..* Aula (Composición): Un grado contiene una o más aulas.
 *   La composición implica que las aulas no pueden existir sin el grado.
 * 
 * MÉTODOS DEL DIAGRAMA UML:
 * + agregarAula(): Añade un aula al grado.
 * 
 * TIPO DE RELACIÓN CON AULA: COMPOSICIÓN
 * Las aulas son parte integral del grado. Si se elimina el grado,
 * las aulas asociadas también deberían eliminarse (orphanRemoval = true).
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "grados")
public class Grado implements Serializable {

    /**
     * serialVersionUID: Para serialización compatible.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: idGrado
     * TIPO: Integer
     * DESCRIPCIÓN: Identificador único del grado (Primary Key).
     * ANOTACIONES:
     * - @Id: Clave primaria.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremental.
     * - @Column(name = "id_grado"): Nombre explícito de columna.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grado")
    private Integer idGrado;

    /**
     * ATRIBUTO: nivel
     * TIPO: String
     * DESCRIPCIÓN: Nivel educativo del grado.
     * Ejemplos: "Primario", "Secundario", "Inicial", "Terciario".
     * También puede incluir año: "1° Año", "2° Año", etc.
     * ANOTACIONES:
     * - @Column(nullable = false, length = 50): Obligatorio, máximo 50 caracteres.
     */
    @Column(nullable = false, length = 50)
    private String nivel;

    // =========================================================================
    // RELACIONES CON OTRAS ENTIDADES
    // =========================================================================

    /**
     * RELACIÓN: Colegio -- Grado (Agregación * -- 1)
     * DESCRIPCIÓN: Múltiples grados pertenecen a un colegio.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Relación muchos-a-uno.
     * - @JoinColumn(name = "id_colegio", nullable = false): FK hacia colegios.
     *   optional = false significa que todo grado debe pertenecer a un colegio.
     * - @ToString.Exclude, @EqualsAndHashCode.Exclude: Evita recursión infinita
     *   en toString() y equals()/hashCode() por referencia circular.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colegio", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Colegio colegio;

    /**
     * RELACIÓN: Grado -- Aula (Composición 1 -- 1..*)
     * DESCRIPCIÓN: Un grado contiene una o más aulas/divisiones.
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "grado", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad Aula es dueña de la relación (tiene la FK id_grado).
     * - cascade = CascadeType.ALL: Propaga todas las operaciones (PERSIST, MERGE, REMOVE).
     *   Si se elimina un grado, también se eliminan sus aulas (composición).
     * - orphanRemoval = true: Elimina aulas que quedan huérfanas (sin grado).
     *   Esto implementa el comportamiento de composición del diagrama UML.
     */
    @OneToMany(mappedBy = "grado", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Aula> aulas = new ArrayList<>();

    // =========================================================================
    // MÉTODOS ESPECÍFICOS (según diagrama UML)
    // =========================================================================

    /**
     * MÉTODO: agregarAula
     * DESCRIPCIÓN: Añade un aula al grado.
     * Método requerido por el diagrama UML.
     * Mantiene la consistencia bidireccional de la relación.
     * 
     * @param aula Aula a agregar al grado
     */
    public void agregarAula(Aula aula) {
        if (!this.aulas.contains(aula)) {
            this.aulas.add(aula);
            aula.setGrado(this);
        }
    }

    /**
     * MÉTODO: removerAula
     * DESCRIPCIÓN: Elimina un aula del grado.
     * 
     * @param aula Aula a remover
     */
    public void removerAula(Aula aula) {
        this.aulas.remove(aula);
        aula.setGrado(null);
    }

    /**
     * MÉTODO: getCantidadAulas
     * DESCRIPCIÓN: Retorna la cantidad total de aulas en este grado.
     * Útil para estadísticas y validaciones.
     * 
     * @return int Número de aulas
     */
    public int getCantidadAulas() {
        return this.aulas != null ? this.aulas.size() : 0;
    }

    /**
     * MÉTODO: getCantidadTotalAlumnos
     * DESCRIPCIÓN: Calcula la cantidad total de alumnos en todas las aulas del grado.
     * Itera sobre cada aula y suma la cantidad de alumnos.
     * 
     * @return int Total de alumnos en el grado
     */
    public int getCantidadTotalAlumnos() {
        if (this.aulas == null || this.aulas.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (Aula aula : this.aulas) {
            total += aula.obtenerCantidadAlumnos();
        }
        return total;
    }

    /**
     * MÉTODO: getNombreCompleto
     * DESCRIPCIÓN: Retorna el nombre completo del grado incluyendo el nivel.
     * Formato: "Nivel - Grado X"
     * 
     * @return String con nombre completo
     */
    public String getNombreCompleto() {
        return this.nivel + " - Grado " + this.idGrado;
    }
}
