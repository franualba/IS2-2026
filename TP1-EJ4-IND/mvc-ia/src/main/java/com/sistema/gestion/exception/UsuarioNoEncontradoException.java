package com.sistema.gestion.exception;

/**
 * Excepcion de negocio lanzada cuando se busca un Usuario por id/correo
 * y no existe en la base de datos.
 */
public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
