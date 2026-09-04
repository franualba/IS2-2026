package com.colegio.gestion.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * CLASE: Nota (Entidad)
 * 
 * DESCRIPCIÓN:
 * Representa una calificación/nota obtenida por un alumno en una materia.
 * Las notas permiten llevar el registro académico del desempeño de los alumnos.
 * 
 * Ejemplo: Un alumno puede tener múltiples notas en "Matemáticas":
 * - Nota 1: 8.5 (fecha: 15/03/2024) - Primer parcial
 * - Nota 2: 7.0 (fecha: 20/04/2024) - Segundo parcial
 * - Nota 3: 9.0 (fecha: 10/05/2024) - Examen final
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Entity: Marca como entidad JPA persistente.
 * - @Table(name = "notas"): Nombre explícito de tabla en PostgreSQL.
 * - @Data, @NoArgsConstructor (Lombok): Generación automática de código.
 * 
 * RELACIONES DEL DIAGRAMA UML:
 * - Nota * -- 1 Materia: Una nota pertenece a una materia.
 * - Nota * -- 1 Alumno: Una nota pertenece a un alumno.
 * 
 * SEGÚN DIAGRAMA UML:
 * - Atributos: idNota (int), fecha (Date), valor (float)
 * - Sin métodos específicos definidos en el diagrama.
 * 
 * ESCALA DE CALIFICACIÓN:
 * El sistema utiliza una escala de 0 a 10 (típica en Argentina y otros países).
 * - 0-3: Insuficiente
 * - 4-5: Regular
 * - 6-7: Bueno
 * - 8-9: Muy Bueno
 * - 10: Excelente
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "notas")
public class Nota implements Serializable {

    /**
     * serialVersionUID: Para serialización compatible.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ATRIBUTO: idNota
     * TIPO: Integer
     * DESCRIPCIÓN: Identificador único de la nota (Primary Key).
     * ANOTACIONES:
     * - @Id: Clave primaria.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremental.
     * - @Column(name = "id_nota"): Nombre explícito de columna.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota")
    private Integer idNota;

    /**
     * ATRIBUTO: fecha
     * TIPO: Date
     * DESCRIPCIÓN: Fecha en que se registró la nota.
     * Puede ser la fecha del examen, evaluación o trabajo práctico.
     * 
     * ANOTACIONES:
     * - @Temporal(TemporalType.DATE): Solo guarda la fecha (sin hora).
     * - @Column(nullable = false): Campo obligatorio.
     */
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    /**
     * ATRIBUTO: valor
     * TIPO: BigDecimal
     * DESCRIPCIÓN: Valor numérico de la calificación con precisión decimal.
     * Escala típica: 0.00 a 10.00.
     * 
     * ANOTACIONES:
     * - @Column(nullable = false, precision = 4, scale = 2): 
     *   precision=4 significa máximo 4 dígitos en total.
     *   scale=2 significa 2 dígitos decimales (ej: 10.00).
     * - columnDefinition = "NUMERIC(4,2)": Tipo SQL explícito para PostgreSQL.
     */
    @Column(nullable = false, precision = 4, scale = 2, 
            columnDefinition = "NUMERIC(4,2) DEFAULT 0.00")
    private BigDecimal valor;

    /**
     * ATRIBUTO: descripcion
     * TIPO: String
     * DESCRIPCIÓN: Descripción opcional de la nota.
     * Ejemplo: "Primer Parcial", "Trabajo Práctico N°3", "Examen Final".
     */
    @Column(length = 100)
    private String descripcion;

    // =========================================================================
    // RELACIONES CON OTRAS ENTIDADES
    // =========================================================================

    /**
     * RELACIÓN: Nota -- Materia (Asociación * -- 1)
     * DESCRIPCIÓN: Múltiples notas pertenecen a una misma materia.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Relación muchos-a-uno.
     * - @JoinColumn(name = "id_materia", nullable = false): FK hacia materias.
     *   optional = false significa que toda nota debe pertenecer a una materia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Materia materia;

    /**
     * RELACIÓN: Nota -- Alumno (Asociación * -- 1)
     * DESCRIPCIÓN: Múltiples notas pertenecen a un mismo alumno.
     * ANOTACIONES:
     * - @ManyToOne(fetch = FetchType.LAZY): Relación muchos-a-uno.
     * - @JoinColumn(name = "id_alumno", nullable = false): FK hacia alumnos.
     *   optional = false significa que toda nota debe pertenecer a un alumno.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Alumno alumno;

    // =========================================================================
    // MÉTODOS AUXILIARES
    // =========================================================================

    /**
     * MÉTODO: esAprobatoria
     * DESCRIPCIÓN: Verifica si la nota es aprobatoria (>= 6).
     * La escala de aprobación típica es 6 o más.
     * 
     * @return boolean true si la nota es >= 6, false en caso contrario
     */
    public boolean esAprobatoria() {
        return this.valor != null && this.valor.compareTo(new BigDecimal("6.0")) >= 0;
    }

    /**
     * MÉTODO: esInsuficiente
     * DESCRIPCIÓN: Verifica si la nota es insuficiente (< 6).
     * 
     * @return boolean true si la nota es < 6, false en caso contrario
     */
    public boolean esInsuficiente() {
        return this.valor != null && this.valor.compareTo(new BigDecimal("6.0")) < 0;
    }

    /**
     * MÉTODO: getEstado
     * DESCRIPCIÓN: Retorna el estado de la nota como texto.
     * 
     * @return String "Aprobado" si >= 6, "Insuficiente" si < 6
     */
    public String getEstado() {
        return this.esAprobatoria() ? "Aprobado" : "Insuficiente";
    }

    /**
     * MÉTODO: getValorConDecimal
     * DESCRIPCIÓN: Retorna el valor formateado con 2 decimales.
     * Útil para mostrar en vistas.
     * 
     * @return String con formato "X.XX"
     */
    public String getValorConDecimal() {
        if (this.valor == null) {
            return "0.00";
        }
        return this.valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * MÉTODO: getFechaFormateada
     * DESCRIPCIÓN: Retorna la fecha formateada como String.
     * Formato: dd/MM/yyyy
     * 
     * @return String con fecha formateada
     */
    public String getFechaFormateada() {
        if (this.fecha == null) {
            return "";
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.fecha);
    }
}
