package com.colegio.gestion.entity;

import java.util.ArrayList;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * CLASE: Profesor (Entidad - Subclase de Persona)
 * 
 * DESCRIPCIÓN:
 * Representa a un profesor del colegio. Hereda los atributos nombre y apellido
 * de la clase base Persona mediante herencia JPA.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Entity: Marca esta clase como entidad JPA que se mapeará a una tabla.
 * - @Table(name = "profesores"): Especifica el nombre de la tabla en la BD.
 * - @Inheritance(strategy = InheritanceType.JOINED): Herencia con tabla propia.
 *   La tabla 'profesores' tendrá idProfesor como PK y FK referenciando a persona.
 * - @Data, @NoArgsConstructor (Lombok): Generación automática de código boilerplate.
 * - @EqualsAndHashCode(callSuper = true): Incluye campos de la superclase en equals/hashCode.
 * 
 * RELACIONES:
 * - Colegio 1 -- * Profesor (Agregación): Un colegio tiene muchos profesores.
 * - Materia * -- 1 Profesor (Asociación): Una materia es dictada por un profesor.
 * - DictadoClases * -- 1 Profesor: Un dictado de clases es asignado a un profesor.
 * 
 * MÉTODOS DEL DIAGRAMA UML:
 * + registrarProfesor(): Crea un nuevo profesor en el sistema.
 * + editarProfesor(): Actualiza los datos de un profesor existente.
 * + eliminarProfesor(): Marca al profesor como eliminado (soft delete).
 * 
 * SEGURIDAD:
 * Los profesores pueden autenticarse en el sistema usando su email como username.
 * El campo 'email' actúa como identificador único para login.
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "profesores")
@Inheritance(strategy = InheritanceType.JOINED)
public class Profesor extends Persona {

    /**
     * serialVersionUID: Para serialización compatible con la superclase.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: idProfesor
     * TIPO: Integer
     * DESCRIPCIÓN: Identificador único del profesor (Primary Key).
     * ANOTACIONES:
     * - @Id: Marca este campo como clave primaria.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremental.
     *   PostgreSQL usa SEQUENCE por defecto, pero IDENTITY funciona con serial.
     * - @Column(name = "id_profesor"): Nombre explícito de la columna.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Integer idProfesor;

    /**
     * ATRIBUTO: especialidad
     * TIPO: String
     * DESCRIPCIÓN: Área de especialización del profesor (ej: Matemáticas, Física).
     * ANOTACIONES:
     * - @Column(length = 100): Máximo 100 caracteres.
     */
    @Column(length = 100)
    private String especialidad;

    /**
     * ATRIBUTO: eliminado
     * TIPO: boolean
     * DESCRIPCIÓN: Bandera para soft delete (eliminación lógica).
     * Cuando es true, el profesor no se muestra en listados pero permanece en BD.
     * ANOTACIONES:
     * - @Column(nullable = false): No puede ser nulo.
     * - @Column(columnDefinition = "boolean default false"): Valor por defecto false.
     */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean eliminado = false;

    /**
     * ATRIBUTO: email
     * TIPO: String
     * DESCRIPCIÓN: Correo electrónico personal del docente.
     * Se utiliza como username para autenticación en Spring Security.
     * ANOTACIONES:
     * - @Column(unique = true, nullable = false): Debe ser único y obligatorio.
     */
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    /**
     * ATRIBUTO: password
     * TIPO: String
     * DESCRIPCIÓN: Contraseña encriptada del profesor.
     * Se almacena usando BCrypt con strength=12 para seguridad.
     * ANOTACIONES:
     * - @Column(nullable = false, length = 255): Obligatorio, longitud para BCrypt.
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * ATRIBUTO: tokenVerificacion
     * TIPO: String
     * DESCRIPCIÓN: Token único para verificación de cuenta por email.
     * Se genera al registrar y se envía por correo para activación.
     */
    @Column(length = 64)
    private String tokenVerificacion;

    /**
     * ATRIBUTO: verificado
     * TIPO: boolean
     * DESCRIPCIÓN: Indica si el profesor verificó su email.
     */
    @Column(columnDefinition = "boolean default false")
    private boolean verificado = false;

    // =========================================================================
    // RELACIONES CON OTRAS ENTIDADES
    // =========================================================================

    /**
     * RELACIÓN: Colegio -- Profesor (Agregación 1 -- *)
     * DESCRIPCIÓN: Un colegio tiene muchos profesores asociados.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Múltiples profesores pertenecen a un colegio.
     *   LAZY carga el colegio solo cuando se accede explícitamente (mejora performance).
     * - @JoinColumn(name = "id_colegio"): Columna FK en la tabla profesores.
     * - cascade = {}: No se propagan operaciones (el colegio existe independientemente).
     * - optional = false: Todo profesor debe pertenecer a un colegio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colegio", nullable = false)
    @ToString.Exclude  // Evita recursión infinita en toString()
    @EqualsAndHashCode.Exclude  // Excluye de equals/hashCode para evitar ciclos
    private Colegio colegio;

    /**
     * RELACIÓN: Materia -- Profesor (Asociación * -- 1)
     * DESCRIPCIÓN: Un profesor puede dictar múltiples materias.
     * ANOTACIONES:
     * - @OneToMany(mappedBy = "profesor", fetch = FetchType.LAZY): Relación inversa.
     *   'mappedBy' indica que la otra entidad (Materia) es la dueña de la relación.
     * - cascade = CascadeType.ALL: Propaga persistencia, merge, remove, etc.
     *   Si se elimina un profesor, también se eliminan sus materias asociadas.
     * - orphanRemoval = true: Elimina materias huérfanas (sin profesor).
     */
    @OneToMany(mappedBy = "profesor", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Materia> materias = new ArrayList<>();

    /**
     * RELACIÓN: DictadoClases -- Profesor
     * DESCRIPCIÓN: Un profesor puede tener múltiples dictados de clases.
     */
    @OneToMany(mappedBy = "profesor", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DictadoClases> dictados = new ArrayList<>();

    // =========================================================================
    // MÉTODOS ESPECÍFICOS (según diagrama UML)
    // =========================================================================

    /**
     * MÉTODO: registrarProfesor
     * DESCRIPCIÓN: Registra un nuevo profesor en el sistema.
     * Este método es implementado en el servicio (ProfesorService).
     * En la entidad, preparamos los datos iniciales.
     * 
     * NOTA: La lógica real está en la capa de servicio para separar
     * responsabilidades (principio Single Responsibility).
     */
    public void inicializarProfesor() {
        this.eliminado = false;
        this.verificado = false;
        if (this.materias == null) {
            this.materias = new ArrayList<>();
        }
        if (this.dictados == null) {
            this.dictados = new ArrayList<>();
        }
    }

    /**
     * MÉTODO: editarProfesor
     * DESCRIPCIÓN: Actualiza los datos del profesor.
     * Los campos se actualizan directamente y JPA detecta los cambios.
     * 
     * @param nuevaEspecialidad Nueva especialidad del profesor
     * @param nuevoEmail Nuevo email (debe validarse unicidad)
     */
    public void actualizarDatos(String nuevaEspecialidad, String nuevoEmail) {
        if (nuevaEspecialidad != null && !nuevaEspecialidad.isEmpty()) {
            this.especialidad = nuevaEspecialidad;
        }
        if (nuevoEmail != null && !nuevoEmail.isEmpty()) {
            this.email = nuevoEmail;
        }
    }

    /**
     * MÉTODO: eliminarProfesor
     * DESCRIPCIÓN: Realiza soft delete marcando el profesor como eliminado.
     * No borra físicamente el registro de la base de datos.
     * Útil para mantener historial y auditoría.
     */
    public void eliminarProfesor() {
        this.eliminado = true;
    }

    /**
     * MÉTODO: agregarMateria
     * DESCRIPCIÓN: Añade una materia a la lista del profesor.
     * Método helper para mantener consistencia bidireccional.
     * 
     * @param materia Materia a agregar
     */
    public void agregarMateria(Materia materia) {
        this.materias.add(materia);
        materia.setProfesor(this);
    }

    /**
     * MÉTODO: removerMateria
     * DESCRIPCIÓN: Elimina una materia de la lista del profesor.
     * 
     * @param materia Materia a remover
     */
    public void removerMateria(Materia materia) {
        this.materias.remove(materia);
        materia.setProfesor(null);
    }

    /**
     * MÉTODO: getNombreCompleto
     * DESCRIPCIÓN: Override del método de Persona para incluir información extra.
     * 
     * @return Nombre completo con especialidad
     */
    @Override
    public String getNombreCompleto() {
        return super.getNombreCompleto() + " (" + this.especialidad + ")";
    }
}
