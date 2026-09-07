package com.colegio.modelo;

/**
 * ENUMERADO requerido por el nuevo registro docente ("Sexo").
 * @Enumerated(EnumType.STRING) lo persiste como texto legible en PostgreSQL.
 */
public enum Sexo {
    MASCULINO, FEMENINO, OTRO
}