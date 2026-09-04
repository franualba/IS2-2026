package com.colegio.gestion.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * CLASE: Alumno (Entidad - Subclase de Persona)
 * 
 * DESCRIPCIÓN:
 * Representa a un alumno del colegio. Hereda los atributos nombre y apellido
 * de la clase base Persona mediante herencia JPA.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Entity: Marca esta clase como entidad JPA mapeable a tabla.
 * - @Table(name = "alumnos"): Nombre explícito de la tabla en PostgreSQL.
 * - @Inheritance(strategy = InheritanceType.JOINED): Herencia con tabla propia.
 *   La tabla 'alumnos' tiene idAlumno como PK y FK a persona.
 * - @Data, @NoArgsConstructor (Lombok): Generación automática de código.
 * - @EqualsAndHashCode(callSuper = true): Incluye campos heredados.
 * 
 * RELACIONES DEL DIAGRAMA UML:
 * - Aula 1 -- 1..* Alumno: Un aula contiene uno o más alumnos.
 * - Materia * -- 1 Alumno: Un alumno cursa múltiples materias.
 * 
 * MÉTODOS DEL DIAGRAMA UML:
 * + registrarAlumno(): Crea un nuevo alumno en el sistema.
 * + editarAlumno(): Actualiza datos del alumno.
 * + eliminarAlumno(): Soft delete del alumno.
 * + listarNotas(): Collection<Nota>: Retorna todas las notas del alumno.
 * 
 * AUDITORÍA:
 * Hereda campos de auditoría de Persona (@CreatedDate, @LastModifiedDate, etc.)
 * que se llenan automáticamente al persistir/modificar.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "alumnos")
@Inheritance(strategy = InheritanceType.JOINED)
public class Alumno extends Persona {

    /**
     * serialVersionUID: Para serialización compatible.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: idAlumno
     * TIPO: Integer
     * DESCRIPCIÓN: Identificador único del alumno (Primary Key).
     * ANOTACIONES:
     * - @Id: Clave primaria.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremental.
     * - @Column(name = "id_alumno"): Nombre de columna explícito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno")
    private Integer idAlumno;

    /**
     * ATRIBUTO: fechaNacimiento
     * TIPO: Date
     * DESCRIPCIÓN: Fecha de nacimiento del alumno.
     * Requerido según diagrama UML para cálculo de edad.
     * ANOTACIONES:
     * - @Temporal(TemporalType.DATE): Solo guarda la fecha (sin hora).
     * - @Column(nullable = false): Campo obligatorio.
     */
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaNacimiento;

    /**
     * ATRIBUTO: eliminado
     * TIPO: boolean
     * DESCRIPCIÓN: Bandera para soft delete (eliminación lógica).
     * Cuando es true, el alumno no aparece en listados activos.
     * ANOTACIONES:
     * - @Column(nullable = false): No nulo.
     * - columnDefinition = "boolean default false": Valor por defecto.
     */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean eliminado = false;

    /**
     * ATRIBUTO: legajo
     * TIPO: String
     * DESCRIPCIÓN: Número de legajo único del alumno.
     * Formato: Año de ingreso + número secuencial.
     */
    @Column(unique = true, length = 20)
    private String legajo;

    // =========================================================================
    // RELACIONES CON OTRAS ENTIDADES
    // =========================================================================

    /**
     * RELACIÓN: Aula -- Alumno (Composición/Asociación 1 -- 1..*)
     * DESCRIPCIÓN: Un alumno pertenece a un aula específica.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Múltiples alumnos en un aula.
     * - @JoinColumn(name = "id_aula"): FK en tabla alumnos.
     * - optional = false: Todo alumno debe tener un aula asignada.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aula", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Aula aula;

    /**
     * RELACIÓN: Materia -- Alumno (Asociación * -- *)
     * DESCRIPCIÓN: Un alumno cursa múltiples materias, una materia tiene muchos alumnos.
     * Es una relación muchos-a-muchos que requiere tabla intermedia.
     * ANOTACIONES:
     * - @ManyToMany(fetch = FetchType.LAZY): Relación N:M.
     * - @JoinTable: Define la tabla intermedia 'alumnos_materias'.
     *   - joinColumns: FK hacia alumnos (lado dueño).
     *   - inverseJoinColumns: FK hacia materias.
     * - cascade = {}: No propaga operaciones (las materias existen independientemente).
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "alumnos_materias",
        joinColumns = @JoinColumn(name = "id_alumno"),
        inverseJoinColumns = @JoinColumn(name = "id_materia")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Materia> materias = new ArrayList<>();

    /**
     * RELACIÓN: Nota -- Alumno (Asociación 1 -- *)
     * DESCRIPCIÓN: Un alumno tiene múltiples notas en diferentes materias.
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad Nota es dueña de la relación (tiene la FK).
     * - cascade = CascadeType.ALL: Propaga operaciones (si se elimina alumno, sus notas también).
     * - orphanRemoval = true: Elimina notas huérfanas.
     */
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Nota> notas = new ArrayList<>();

    // =========================================================================
    // MÉTODOS ESPECÍFICOS (según diagrama UML)
    // =========================================================================

    /**
     * MÉTODO: registrarAlumno
     * DESCRIPCIÓN: Inicializa un nuevo alumno en el sistema.
     * La lógica completa está en la capa de servicio.
     */
    public void inicializarAlumno() {
        this.eliminado = false;
        if (this.materias == null) {
            this.materias = new ArrayList<>();
        }
        if (this.notas == null) {
            this.notas = new ArrayList<>();
        }
    }

    /**
     * MÉTODO: editarAlumno
     * DESCRIPCIÓN: Actualiza los datos del alumno.
     * 
     * @param nuevaFechaNacimiento Nueva fecha de nacimiento
     * @param nuevoLegajo Nuevo número de legajo
     */
    public void actualizarDatos(Date nuevaFechaNacimiento, String nuevoLegajo) {
        if (nuevaFechaNacimiento != null) {
            this.fechaNacimiento = nuevaFechaNacimiento;
        }
        if (nuevoLegajo != null && !nuevoLegajo.isEmpty()) {
            this.legajo = nuevoLegajo;
        }
    }

    /**
     * MÉTODO: eliminarAlumno
     * DESCRIPCIÓN: Realiza soft delete marcando al alumno como eliminado.
     * Mantiene el registro en BD para historial y auditoría.
     */
    public void eliminarAlumno() {
        this.eliminado = true;
    }

    /**
     * MÉTODO: listarNotas
     * DESCRIPCIÓN: Retorna todas las notas del alumno.
     * Método requerido por el diagrama UML.
     * 
     * @return Collection<Nota> Lista inmutable de notas
     */
    public Collection<Nota> listarNotas() {
        if (this.notas == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.notas);
    }

    /**
     * MÉTODO: agregarMateria
     * DESCRIPCIÓN: Añade una materia al alumno.
     * Método helper para mantener consistencia bidireccional.
     * 
     * @param materia Materia a agregar
     */
    public void agregarMateria(Materia materia) {
        if (!this.materias.contains(materia)) {
            this.materias.add(materia);
        }
    }

    /**
     * MÉTODO: removerMateria
     * DESCRIPCIÓN: Elimina una materia de la lista del alumno.
     * 
     * @param materia Materia a remover
     */
    public void removerMateria(Materia materia) {
        this.materias.remove(materia);
    }

    /**
     * MÉTODO: calcularEdad
     * DESCRIPCIÓN: Calcula la edad actual del alumno basada en fechaNacimiento.
     * Útil para validaciones de edad mínima/máxima.
     * 
     * @return int Edad en años
     */
    public int calcularEdad() {
        if (this.fechaNacimiento == null) {
            return 0;
        }
        long diferenciaMillis = System.currentTimeMillis() - this.fechaNacimiento.getTime();
        return (int) (diferenciaMillis / (1000L * 60 * 60 * 24 * 365.25));
    }

    /**
     * MÉTODO: getPromedioGeneral
     * DESCRIPCIÓN: Calcula el promedio de todas las notas del alumno.
     * 
     * @return float Promedio de notas (0 si no tiene notas)
     */
    public float getPromedioGeneral() {
        if (this.notas == null || this.notas.isEmpty()) {
            return 0.0f;
        }
        float suma = 0.0f;
        for (Nota nota : this.notas) {
            suma += nota.getValor();
        }
        return suma / this.notas.size();
    }
}
