package com.sistema.gestion.service;

import com.sistema.gestion.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * SERVICIO (CAPA DE LOGICA DE NEGOCIO) - UsuarioService
 * ============================================================================
 * En la arquitectura MVC "clasica" la capa de Servicio no es una de las 3
 * capas formales (Modelo-Vista-Controlador), pero es una practica estandar
 * de Spring separar la LOGICA DE NEGOCIO (Service) del ACCESO A DATOS
 * (Repository) y de la ORQUESTACION HTTP (Controller). De esta forma el
 * Controller queda "delgado" (solo recibe la request y arma la respuesta)
 * y el Service concentra las reglas (por ejemplo: encriptar password,
 * validar 3 intentos fallidos, etc.), quedando dentro de la capa Modelo
 * en el sentido amplio de MVC.
 * ============================================================================
 */
public interface UsuarioService {

    /** Registra un nuevo usuario (correo unico, password encriptada). */
    Usuario registrar(Usuario usuario);

    /**
     * Intenta iniciar sesion con correo + password en texto plano.
     *
     * @return Optional con el Usuario si el login fue exitoso, o vacio si
     *         la clave es incorrecta / el usuario esta bloqueado.
     */
    Optional<Usuario> iniciarSesion(String correo, String passwordPlano);

    Optional<Usuario> buscarPorCorreo(String correo);

    boolean existeCorreo(String correo);

    boolean existeDocumento(String documento);

    List<Usuario> listarTodos();

    Usuario buscarPorId(Long id);

    /** Usado por un Administrador para desbloquear manualmente a un Usuario. */
    void desbloquear(Long usuarioId);

    void eliminar(Long id);
}
