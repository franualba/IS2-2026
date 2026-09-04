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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

/**
 * CLASE: Materia (Entidad)
 * 
 * DESCRIPCIÓN:
 * Representa una asignatura/materia que se dicta en el colegio.
 * Ejemplos: "Matemáticas", "Lengua y Literatura", "Historia", "Física".
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Entity: Marca como entidad JPA persistente.
 * - @Table(name = "materias"): Nombre explícito de tabla en PostgreSQL.
 * - @Data, @NoArgsConstructor (Lombok): Generación automática de código.
 * 
 * RELACIONES DEL DIAGRAMA UML:
 * - Colegio 1 -- * Materia (Agregación): Un colegio ofrece múltiples materias.
 * - Materia * -- 1 Profesor: Una materia es dictada por un profesor.
 * - Materia * -- 1 Alumno: Una materia es cursada por múltiples alumnos.
 * - Materia 1 -- * DictadoClases: Una materia tiene múltiples dictados de clase.
 * - Materia 1 -- * Nota: Una materia tiene múltiples notas asociadas.
 * 
 * MÉTODOS DEL DIAGRAMA UML:
 * + registrarMateria(): Crea una nueva materia.
 * + editarMateria(): Actualiza datos de la materia.
 * + eliminarMateria(): Soft delete de la materia.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "materias")
public class Materia implements Serializable {

    /**
     * serialVersionUID: Para serialización compatible.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: idMateria
     * TIPO: Integer
     * DESCRIPCIÓN: Identificador único de la materia (Primary Key).
     * ANOTACIONES:
     * - @Id: Clave primaria.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremental.
     * - @Column(name = "id_materia"): Nombre explícito de columna.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia")
    private Integer idMateria;

    /**
     * ATRIBUTO: nombre
     * TIPO: String
     * DESCRIPCIÓN: Nombre de la materia/asignatura.
     * Ejemplos: "Matemáticas", "Geografía", "Química".
     * ANOTACIONES:
     * - @Column(nullable = false, length = 100): Obligatorio, máximo 100 caracteres.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * ATRIBUTO: eliminado
     * TIPO: boolean
     * DESCRIPCIÓN: Bandera para soft delete (eliminación lógica).
     * Cuando es true, la materia no aparece en listados activos.
     * ANOTACIONES:
     * - @Column(nullable = false): No nulo.
     * - columnDefinition = "boolean default false": Valor por defecto false.
     */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean eliminado = false;

    /**
     * ATRIBUTO: anio
     * TIPO: Integer
     * DESCRIPCIÓN: Año académico al que pertenece la materia.
     * Ejemplo: 1, 2, 3, 4, 5, 6 (según nivel educativo).
     */
    @Column
    private Integer anio;

    // =========================================================================
    // RELACIONES CON OTRAS ENTIDADES
    // =========================================================================

    /**
     * RELACIÓN: Colegio -- Materia (Agregación * -- 1)
     * DESCRIPCIÓN: Múltiples materias pertenecen a un colegio.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Relación muchos-a-uno.
     * - @JoinColumn(name = "id_colegio", nullable = false): FK hacia colegios.
     *   optional = false significa que toda materia debe pertenecer a un colegio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colegio", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Colegio colegio;

    /**
     * RELACIÓN: Materia -- Profesor (Asociación * -- 1)
     * DESCRIPCIÓN: Una materia es dictada por un profesor.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Múltiples materias pueden ser
     *   dictadas por el mismo profesor.
     * - @JoinColumn(name = "id_profesor"): FK hacia profesores.
     * - optional = true: Una materia puede no tener profesor asignado aún.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profesor profesor;

    /**
     * RELACIÓN: Materia -- Alumno (Asociación * -- *)
     * DESCRIPCIÓN: Una materia es cursada por múltiples alumnos,
     * y un alumno cursa múltiples materias (relación muchos-a-muchos).
     * ANOTACIONES:
     * - @ManyToMany(mappedBy = "materias", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad Alumno es dueña de la relación (tiene la tabla intermedia).
     * - Esta es la parte inversa de la relación definida en Alumno.
     */
    @ManyToMany(mappedBy = "materias", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Alumno> alumnos = new ArrayList<>();

    /**
     * RELACIÓN: Materia -- DictadoClases (Asociación 1 -- *)
     * DESCRIPCIÓN: Una materia tiene múltiples dictados de clase
     * (instancias de la materia en diferentes años lectivos).
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "materia", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad DictadoClases es dueña de la relación (tiene la FK).
     * - cascade = CascadeType.ALL: Propaga operaciones CRUD.
     * - orphanRemoval = true: Elimina dictados huérfanos.
     */
    @OneToMany(mappedBy = "materia", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DictadoClases> dictados = new ArrayList<>();

    /**
     * RELACIÓN: Materia -- Nota (Asociación 1 -- *)
     * DESCRIPCIÓN: Una materia tiene múltiples notas asociadas.
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "materia", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad Nota es dueña de la relación (tiene la FK id_materia).
     * - cascade = CascadeType.ALL: Propaga operaciones.
     * - orphanRemoval = true: Elimina notas huérfanas.
     */
    @OneToMany(mappedBy = "materia", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Nota> notas = new ArrayList<>();

    // =========================================================================
    // MÉTODOS ESPECÍFICOS (según diagrama UML)
    // =========================================================================

    /**
     * MÉTODO: registrarMateria
     * DESCRIPCIÓN: Inicializa una nueva materia en el sistema.
     * La lógica completa está en la capa de servicio.
     */
    public void inicializarMateria() {
        this.eliminado = false;
        if (this.dictados == null) {
            this.dictados = new ArrayList<>();
        }
        if (this.notas == null) {
            this.notas = new ArrayList<>();
        }
    }

    /**
     * MÉTODO: editarMateria
     * DESCRIPCIÓN: Actualiza los datos de la materia.
     * 
     * @param nuevoNombre Nuevo nombre de la materia
     * @param nuevoAnio Nuevo año académico
     */
    public void actualizarDatos(String nuevoNombre, Integer nuevoAnio) {
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
            this.nombre = nuevoNombre;
        }
        if (nuevoAnio != null) {
            this.anio = nuevoAnio;
        }
    }

    /**
     * MÉTODO: eliminarMateria
     * DESCRIPCIÓN: Realiza soft delete marcando la materia como eliminada.
     * Mantiene el registro en BD para historial y auditoría.
     */
    public void eliminarMateria() {
        this.eliminado = true;
    }

    /**
     * MÉTODO: agregarDictado
     * DESCRIPCIÓN: Añade un dictado de clase a la materia.
     * Método helper para mantener consistencia bidireccional.
     * 
     * @param dictado DictadoClases a agregar
     */
    public void agregarDictado(DictadoClases dictado) {
        if (!this.dictados.contains(dictado)) {
            this.dictados.add(dictado);
            dictado.setMateria(this);
        }
    }

    /**
     * MÉTODO: removerDictado
     * DESCRIPCIÓN: Elimina un dictado de la materia.
     * 
     * @param dictado DictadoClases a remover
     */
    public void removerDictado(DictadoClases dictado) {
        this.dictados.remove(dictado);
        dictado.setMateria(null);
    }

    /**
     * MÉTODO: getCantidadAlumnos
     * DESCRIPCIÓN: Retorna la cantidad total de alumnos cursando esta materia.
     * 
     * @return int Número de alumnos
     */
    public int getCantidadAlumnos() {
        return this.alumnos != null ? this.alumnos.size() : 0;
    }

    /**
     * MÉTODO: getCantidadDictados
     * DESCRIPCIÓN: Retorna la cantidad de dictados de esta materia.
     * 
     * @return int Número de dictados
     */
    public int getCantidadDictados() {
        return this.dictados != null ? this.dictados.size() : 0;
    }
}
