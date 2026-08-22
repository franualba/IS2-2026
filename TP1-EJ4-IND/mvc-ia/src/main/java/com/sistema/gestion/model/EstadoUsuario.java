package com.sistema.gestion.model;

/**
 * ============================================================================
 * MODELO - Enum EstadoUsuario
 * ============================================================================
 * Corresponde al "ENUM estadoUsuario" del diagrama UML, con la relacion
 * "1...*" hacia Usuario (un estado puede aplicar a muchos usuarios).
 *
 * Valores definidos en el UML: ACTIVO, BLOQUEADO.
 * ============================================================================
 */
public enum EstadoUsuario {
    ACTIVO,
    BLOQUEADO
}
