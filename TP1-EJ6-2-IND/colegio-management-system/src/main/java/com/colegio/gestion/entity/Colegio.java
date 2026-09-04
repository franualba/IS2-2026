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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

/**
 * CLASE: Colegio (Entidad)
 * 
 * DESCRIPCIÓN:
 * Representa una institución educativa (colegio/escuela).
 * Es la entidad raíz que contiene grados, profesores y materias.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Entity: Marca como entidad JPA persistente.
 * - @Table(name = "colegios"): Nombre explícito de tabla en PostgreSQL.
 * - @Data, @NoArgsConstructor (Lombok): Generación automática de código boilerplate.
 * 
 * RELACIONES DEL DIAGRAMA UML (AGREGACIÓN):
 * - Colegio 1 -- * Grado: Un colegio tiene múltiples grados (niveles).
 * - Colegio 1 -- * Profesor: Un colegio emplea muchos profesores.
 * - Colegio 1 -- * Materia: Un colegio ofrece múltiples materias.
 * 
 * TIPO DE RELACIÓN: AGREGACIÓN
 * Las entidades relacionadas (Grado, Profesor, Materia) pueden existir
 * independientemente del colegio, pero están asociadas a él.
 * 
 * MÉTODOS DEL DIAGRAMA UML:
 * + registrarColegio(): Crea un nuevo colegio.
 * + editarColegio(): Actualiza datos del colegio.
 * + eliminarColegio(): Soft delete del colegio.
 * 
 * AUDITORÍA:
 * Esta entidad NO hereda de Persona, por lo tanto no tiene auditoría automática.
 * Si se requiere auditoría, se puede implementar manualmente o crear una superclase
 * abstracta con campos de auditoría.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "colegios")
public class Colegio implements Serializable {

    /**
     * serialVersionUID: Para serialización compatible.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: idColegio
     * TIPO: Integer
     * DESCRIPCIÓN: Identificador único del colegio (Primary Key).
     * ANOTACIONES:
     * - @Id: Clave primaria.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremental.
     * - @Column(name = "id_colegio"): Nombre explícito de columna.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colegio")
    private Integer idColegio;

    /**
     * ATRIBUTO: nombre
     * TIPO: String
     * DESCRIPCIÓN: Nombre oficial del colegio.
     * Ejemplo: "Escuela Normal Superior", "Colegio Nacional".
     * ANOTACIONES:
     * - @Column(nullable = false, length = 150): Obligatorio, máximo 150 caracteres.
     */
    @Column(nullable = false, length = 150)
    private String nombre;

    /**
     * ATRIBUTO: direccion
     * TIPO: String
     * DESCRIPCIÓN: Dirección física del colegio (calle, número, ciudad).
     * ANOTACIONES:
     * - @Column(length = 200): Máximo 200 caracteres.
     */
    @Column(length = 200)
    private String direccion;

    /**
     * ATRIBUTO: eliminado
     * TIPO: boolean
     * DESCRIPCIÓN: Bandera para soft delete (eliminación lógica).
     * Cuando es true, el colegio no aparece en listados activos.
     * ANOTACIONES:
     * - @Column(nullable = false): No nulo.
     * - columnDefinition = "boolean default false": Valor por defecto false.
     */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean eliminado = false;

    // =========================================================================
    // RELACIONES CON OTRAS ENTIDADES (AGREGACIÓN)
    // =========================================================================

    /**
     * RELACIÓN: Colegio -- Grado (Agregación 1 -- *)
     * DESCRIPCIÓN: Un colegio tiene múltiples grados/niveles educativos.
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "colegio", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad Grado es dueña de la relación (tiene la FK id_colegio).
     * - cascade = CascadeType.ALL: Propaga operaciones (persist, merge, remove).
     *   Si se elimina un colegio, también se eliminan sus grados.
     * - orphanRemoval = true: Elimina grados huérfanos (sin colegio).
     */
    @OneToMany(mappedBy = "colegio", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Grado> grados = new ArrayList<>();

    /**
     * RELACIÓN: Colegio -- Profesor (Agregación 1 -- *)
     * DESCRIPCIÓN: Un colegio emplea múltiples profesores.
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "colegio", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad Profesor tiene la FK id_colegio.
     * - cascade = CascadeType.ALL: Propaga operaciones CRUD.
     * - orphanRemoval = true: Elimina profesores sin colegio.
     */
    @OneToMany(mappedBy = "colegio", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Profesor> profesores = new ArrayList<>();

    /**
     * RELACIÓN: Colegio -- Materia (Agregación 1 -- *)
     * DESCRIPCIÓN: Un colegio ofrece múltiples materias/assignaturas.
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "colegio", fetch = FetchType.LAZY): Relación inversa.
     *   La entidad Materia tiene la FK id_colegio.
     * - cascade = CascadeType.ALL: Propaga operaciones.
     * - orphanRemoval = true: Elimina materias huérfanas.
     */
    @OneToMany(mappedBy = "colegio", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Materia> materias = new ArrayList<>();

    // =========================================================================
    // MÉTODOS ESPECÍFICOS (según diagrama UML)
    // =========================================================================

    /**
     * MÉTODO: registrarColegio
     * DESCRIPCIÓN: Inicializa un nuevo colegio en el sistema.
     * La lógica completa está en la capa de servicio.
     */
    public void inicializarColegio() {
        this.eliminado = false;
        if (this.grados == null) {
            this.grados = new ArrayList<>();
        }
        if (this.profesores == null) {
            this.profesores = new ArrayList<>();
        }
        if (this.materias == null) {
            this.materias = new ArrayList<>();
        }
    }

    /**
     * MÉTODO: editarColegio
     * DESCRIPCIÓN: Actualiza los datos del colegio.
     * 
     * @param nuevoNombre Nuevo nombre del colegio
     * @param nuevaDireccion Nueva dirección física
     */
    public void actualizarDatos(String nuevoNombre, String nuevaDireccion) {
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
            this.nombre = nuevoNombre;
        }
        if (nuevaDireccion != null) {
            this.direccion = nuevaDireccion;
        }
    }

    /**
     * MÉTODO: eliminarColegio
     * DESCRIPCIÓN: Realiza soft delete marcando al colegio como eliminado.
     * Mantiene el registro en BD para historial y auditoría.
     * NOTA: En producción, verificar que no haya datos dependientes antes de eliminar.
     */
    public void eliminarColegio() {
        this.eliminado = true;
    }

    /**
     * MÉTODO: agregarGrado
     * DESCRIPCIÓN: Añade un grado al colegio.
     * Método helper para mantener consistencia bidireccional.
     * 
     * @param grado Grado a agregar
     */
    public void agregarGrado(Grado grado) {
        if (!this.grados.contains(grado)) {
            this.grados.add(grado);
            grado.setColegio(this);
        }
    }

    /**
     * MÉTODO: removerGrado
     * DESCRIPCIÓN: Elimina un grado del colegio.
     * 
     * @param grado Grado a remover
     */
    public void removerGrado(Grado grado) {
        this.grados.remove(grado);
        grado.setColegio(null);
    }

    /**
     * MÉTODO: agregarProfesor
     * DESCRIPCIÓN: Añade un profesor al colegio.
     * 
     * @param profesor Profesor a agregar
     */
    public void agregarProfesor(Profesor profesor) {
        if (!this.profesores.contains(profesor)) {
            this.profesores.add(profesor);
            profesor.setColegio(this);
        }
    }

    /**
     * MÉTODO: removerProfesor
     * DESCRIPCIÓN: Elimina un profesor del colegio.
     * 
     * @param profesor Profesor a remover
     */
    public void removerProfesor(Profesor profesor) {
        this.profesores.remove(profesor);
        profesor.setColegio(null);
    }

    /**
     * MÉTODO: agregarMateria
     * DESCRIPCIÓN: Añade una materia al catálogo del colegio.
     * 
     * @param materia Materia a agregar
     */
    public void agregarMateria(Materia materia) {
        if (!this.materias.contains(materia)) {
            this.materias.add(materia);
            materia.setColegio(this);
        }
    }

    /**
     * MÉTODO: removerMateria
     * DESCRIPCIÓN: Elimina una materia del catálogo del colegio.
     * 
     * @param materia Materia a remover
     */
    public void removerMateria(Materia materia) {
        this.materias.remove(materia);
        materia.setColegio(null);
    }

    /**
     * MÉTODO: getCantidadGrados
     * DESCRIPCIÓN: Retorna la cantidad total de grados del colegio.
     * 
     * @return int Número de grados
     */
    public int getCantidadGrados() {
        return this.grados != null ? this.grados.size() : 0;
    }

    /**
     * MÉTODO: getCantidadProfesores
     * DESCRIPCIÓN: Retorna la cantidad total de profesores del colegio.
     * 
     * @return int Número de profesores
     */
    public int getCantidadProfesores() {
        return this.profesores != null ? this.profesores.size() : 0;
    }

    /**
     * MÉTODO: getCantidadMaterias
     * DESCRIPCIÓN: Retorna la cantidad total de materias del colegio.
     * 
     * @return int Número de materias
     */
    public int getCantidadMaterias() {
        return this.materias != null ? this.materias.size() : 0;
    }
}
