package com.sistema.gestion.service;

import com.sistema.gestion.model.Administrador;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * SERVICIO - AdministradorService
 * ============================================================================
 * Logica de negocio para el registro/login de Administradores y para la
 * operacion gestionarUsuario()/desbloquearUsuario() definida en el UML.
 * ============================================================================
 */
public interface AdministradorService {

    Administrador registrar(Administrador administrador);

    Optional<Administrador> iniciarSesion(String correo, String passwordPlano);

    Optional<Administrador> buscarPorCorreo(String correo);

    List<Administrador> listarTodos();

    /** +desbloquearUsuario(): void (delegado a UsuarioService.desbloquear) */
    void desbloquearUsuario(Long usuarioId);
}
