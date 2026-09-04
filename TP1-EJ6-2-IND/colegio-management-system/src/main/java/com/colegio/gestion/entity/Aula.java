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
 * CLASE: Aula (Entidad)
 * 
 * DESCRIPCIÓN:
 * Representa un aula específica dentro de un grado (división/sección).
 * Ejemplo: "1°A", "2°B", "3°C" - donde el número indica el año y la letra la división.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Entity: Marca como entidad JPA persistente.
 * - @Table(name = "aulas"): Nombre explícito de tabla en PostgreSQL.
 * - @Data, @NoArgsConstructor (Lombok): Generación automática de código boilerplate.
 * 
 * RELACIONES DEL DIAGRAMA UML:
 * - Grado 1 -- 1..* Aula (Composición): Un grado contiene una o más aulas.
 *   El aula no puede existir sin un grado padre.
 * - Aula 1 -- 1..* Alumno: Un aula contiene uno o más alumnos.
 * 
 * MÉTODOS DEL DIAGRAMA UML:
 * + obtenerCantidadAlumnos(): int - Retorna el número de alumnos en el aula.
 * 
 * COMPOSICIÓN VS AGREGACIÓN:
 * La relación Grado-Aula es de COMPOSICIÓN porque:
 * 1. Las aulas no tienen sentido sin un grado al que pertenecer.
 * 2. Si se elimina el grado, las aulas también deberían eliminarse.
 * 3. El ciclo de vida del aula está ligado al del grado.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "aulas")
public class Aula implements Serializable {

    /**
     * serialVersionUID: Para serialización compatible.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: idAula
     * TIPO: Integer
     * DESCRIPCIÓN: Identificador único del aula (Primary Key).
     * ANOTACIONES:
     * - @Id: Clave primaria.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremental.
     * - @Column(name = "id_aula"): Nombre explícito de columna.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private Integer idAula;

    /**
     * ATRIBUTO: division
     * TIPO: String
     * DESCRIPCIÓN: División o sección del aula.
     * Ejemplos: "A", "B", "C", "1", "2", "Única".
     * Se combina con el nivel del grado para identificar el aula.
     * ANOTACIONES:
     * - @Column(nullable = false, length = 20): Obligatorio, máximo 20 caracteres.
     */
    @Column(nullable = false, length = 20)
    private String division;

    // =========================================================================
    // RELACIONES CON OTRAS ENTIDADES
    // =========================================================================

    /**
     * RELACIÓN: Grado -- Aula (Composición * -- 1)
     * DESCRIPCIÓN: Múltiples aulas pertenecen a un grado.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Relación muchos-a-uno.
     * - @JoinColumn(name = "id_grado", nullable = false): FK hacia grados.
     *   optional = false significa que toda aula debe pertenecer a un grado.
     * - Esta es la parte "hija" de la relación de composición.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Grado grado;

    /**
     * RELACIÓN: Aula -- Alumno (Asociación 1 -- 1..*)
     * DESCRIPCIÓN: Un aula contiene uno o más alumnos.
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "aula", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad Alumno es dueña de la relación (tiene la FK id_aula).
     * - cascade = {}: No propaga operaciones automáticamente.
     *   Los alumnos pueden cambiar de aula sin eliminarlos.
     * - orphanRemoval = false: Los alumnos no se eliminan si cambian de aula.
     */
    @OneToMany(mappedBy = "aula", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Alumno> alumnos = new ArrayList<>();

    // =========================================================================
    // MÉTODOS ESPECÍFICOS (según diagrama UML)
    // =========================================================================

    /**
     * MÉTODO: obtenerCantidadAlumnos
     * DESCRIPCIÓN: Retorna la cantidad actual de alumnos en el aula.
     * Método requerido por el diagrama UML.
     * 
     * @return int Número de alumnos inscritos en el aula
     */
    public int obtenerCantidadAlumnos() {
        return this.alumnos != null ? this.alumnos.size() : 0;
    }

    /**
     * MÉTODO: agregarAlumno
     * DESCRIPCIÓN: Añade un alumno al aula.
     * Método helper para mantener consistencia bidireccional.
     * 
     * @param alumno Alumno a agregar
     */
    public void agregarAlumno(Alumno alumno) {
        if (!this.alumnos.contains(alumno)) {
            this.alumnos.add(alumno);
            alumno.setAula(this);
        }
    }

    /**
     * MÉTODO: removerAlumno
     * DESCRIPCIÓN: Elimina un alumno del aula.
     * Nota: Esto no elimina al alumno del sistema, solo lo desvincula del aula.
     * 
     * @param alumno Alumno a remover
     */
    public void removerAlumno(Alumno alumno) {
        this.alumnos.remove(alumno);
        alumno.setAula(null);
    }

    /**
     * MÉTODO: getNombreCompleto
     * DESCRIPCIÓN: Retorna el nombre completo del aula combinando grado y división.
     * Formato: "Nivel - División" (ej: "Secundario - A")
     * 
     * @return String con nombre completo del aula
     */
    public String getNombreCompleto() {
        if (this.grado != null) {
            return this.grado.getNivel() + " - " + this.division;
        }
        return this.division;
    }

    /**
     * MÉTODO: tieneCupo
     * DESCRIPCIÓN: Verifica si el aula tiene cupo disponible.
     * Se puede definir un límite máximo de alumnos por aula.
     * 
     * @param limiteMaximo Máximo de alumnos permitidos
     * @return boolean true si hay cupo, false si está llena
     */
    public boolean tieneCupo(int limiteMaximo) {
        return this.obtenerCantidadAlumnos() < limiteMaximo;
    }

    /**
     * MÉTODO: getListadoAlumnos
     * DESCRIPCIÓN: Retorna una copia de la lista de alumnos.
     * Útil para evitar modificaciones externas directas.
     * 
     * @return List<Alumno> Copia de la lista de alumnos
     */
    public List<Alumno> getListadoAlumnos() {
        return new ArrayList<>(this.alumnos);
    }
}
