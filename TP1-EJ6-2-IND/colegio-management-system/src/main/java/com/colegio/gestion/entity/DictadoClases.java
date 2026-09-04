package com.colegio.gestion.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

/**
 * CLASE: DictadoClases (Entidad)
 * 
 * DESCRIPCIÓN:
 * Representa una instancia de dictado de una materia en un año lectivo específico.
 * Esta entidad permite llevar el historial de qué materias se dictaron en cada
 * año académico, y qué profesor las dictó.
 * 
 * Ejemplo: La materia "Matemáticas" puede tener múltiples dictados:
 * - Dictado 2023 (año lectivo 2023)
 * - Dictado 2024 (año lectivo 2024)
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Entity: Marca como entidad JPA persistente.
 * - @Table(name = "dictado_clases"): Nombre explícito de tabla en PostgreSQL.
 * - @Data, @NoArgsConstructor (Lombok): Generación automática de código.
 * 
 * RELACIONES DEL DIAGRAMA UML:
 * - DictadoClases * -- 1 Materia: Un dictado pertenece a una materia.
 * - DictadoClases * -- 1 Profesor: Un dictado es asignado a un profesor.
 * 
 * MÉTODOS DEL DIAGRAMA UML:
 * + asignarProfesor(): Asigna un profesor al dictado.
 * + listarAlumnos(): Lista los alumnos inscriptos en este dictado.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "dictado_clases")
public class DictadoClases implements Serializable {

    /**
     * serialVersionUID: Para serialización compatible.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: idDictado
     * TIPO: Integer
     * DESCRIPCIÓN: Identificador único del dictado de clases (Primary Key).
     * ANOTACIONES:
     * - @Id: Clave primaria.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremental.
     * - @Column(name = "id_dictado"): Nombre explícito de columna.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dictado")
    private Integer idDictado;

    /**
     * ATRIBUTO: anioLectivo
     * TIPO: Date
     * DESCRIPCIÓN: Año lectivo al que corresponde este dictado.
     * Se usa Date para permitir rangos de fechas o año completo.
     * Ejemplo: 01/01/2024 (representa el año 2024).
     * 
     * ANOTACIONES:
     * - @Temporal(TemporalType.DATE): Solo guarda la fecha (sin hora).
     * - @Column(nullable = false): Campo obligatorio.
     */
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date anioLectivo;

    // =========================================================================
    // RELACIONES CON OTRAS ENTIDADES
    // =========================================================================

    /**
     * RELACIÓN: DictadoClases -- Materia (Asociación * -- 1)
     * DESCRIPCIÓN: Múltiples dictados pueden pertenecer a una misma materia
     * (uno por cada año lectivo).
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Relación muchos-a-uno.
     * - @JoinColumn(name = "id_materia", nullable = false): FK hacia materias.
     *   optional = false significa que todo dictado debe tener una materia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Materia materia;

    /**
     * RELACIÓN: DictadoClases -- Profesor (Asociación * -- 1)
     * DESCRIPCIÓN: Un dictado es asignado a un profesor específico.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Múltiples dictados pueden ser
     *   asignados al mismo profesor.
     * - @JoinColumn(name = "id_profesor", nullable = false): FK hacia profesores.
     *   optional = false significa que todo dictado debe tener un profesor asignado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profesor profesor;

    // =========================================================================
    // MÉTODOS ESPECÍFICOS (según diagrama UML)
    // =========================================================================

    /**
     * MÉTODO: asignarProfesor
     * DESCRIPCIÓN: Asigna un profesor a este dictado de clase.
     * Método requerido por el diagrama UML.
     * 
     * @param profesor El profesor que dictará esta materia
     */
    public void asignarProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    /**
     * MÉTODO: listarAlumnos
     * DESCRIPCIÓN: Retorna la lista de alumnos inscriptos en este dictado.
     * Obtiene los alumnos desde la materia asociada.
     * Nota: Para una implementación más precisa, se podría agregar una relación
     * directa entre DictadoClases y Alumno mediante una tabla intermedia.
     * 
     * @return int Cantidad de alumnos (desde la materia asociada)
     */
    public int listarAlumnos() {
        if (this.materia != null) {
            return this.materia.getCantidadAlumnos();
        }
        return 0;
    }

    /**
     * MÉTODO: getAnioLectivoString
     * DESCRIPCIÓN: Retorna el año lectivo como String (solo el año).
     * Útil para mostrar en vistas.
     * 
     * @return String con el año (ej: "2024")
     */
    public String getAnioLectivoString() {
        if (this.anioLectivo == null) {
            return "";
        }
        // Extraer solo el año de la fecha
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
        return sdf.format(this.anioLectivo);
    }

    /**
     * MÉTODO: getDescripcionCompleta
     * DESCRIPCIÓN: Retorna una descripción completa del dictado.
     * Formato: "Materia - Año Lectivo - Profesor"
     * 
     * @return String con descripción completa
     */
    public String getDescripcionCompleta() {
        String nombreMateria = this.materia != null ? this.materia.getNombre() : "Sin materia";
        String nombreProfesor = this.profesor != null ? this.profesor.getNombreCompleto() : "Sin profesor";
        String anio = this.getAnioLectivoString();
        
        return nombreMateria + " (" + anio + ") - " + nombreProfesor;
    }
}
