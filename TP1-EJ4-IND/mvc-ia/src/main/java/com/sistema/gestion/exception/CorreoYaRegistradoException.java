package com.sistema.gestion.exception;

/**
 * Excepcion de negocio lanzada al intentar registrar una Persona
 * (Usuario o Administrador) con un correo que ya existe en el sistema.
 * Recordar que el correo funciona como "usuario" (login).
 */
public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException(String mensaje) {
        super(mensaje);
    }
}
