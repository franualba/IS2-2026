package com.sistema.gestion.controller;

import com.sistema.gestion.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ============================================================================
 * CONTROLADOR (CAPA C de MVC) - UsuarioController
 * ============================================================================
 * Rutas de administracion de Usuarios, reservadas para el rol Administrador
 * (protegidas por AuthInterceptor bajo el prefijo "/admin"). Implementa
 * "gestionarUsuario()" y "desbloquearUsuario()" del UML desde la perspectiva
 * de la capa web.
 * ============================================================================
 */
@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** Lista todos los usuarios registrados, con su estado (activo/bloqueado). */
    @GetMapping("/admin/usuarios")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/list";
    }

    /** Administrador.desbloquearUsuario(): reactiva a un usuario bloqueado. */
    @PostMapping("/admin/usuarios/{id}/desbloquear")
    public String desbloquear(@PathVariable Long id) {
        usuarioService.desbloquear(id);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/admin/usuarios/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/admin/usuarios";
    }
}
