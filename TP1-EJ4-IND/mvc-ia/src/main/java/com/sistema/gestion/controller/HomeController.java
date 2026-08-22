package com.sistema.gestion.controller;

import com.sistema.gestion.model.Administrador;
import com.sistema.gestion.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ============================================================================
 * CONTROLADOR (CAPA C de MVC) - HomeController
 * ============================================================================
 * Pagina principal luego de iniciar sesion, con accesos rapidos a los
 * distintos modulos (Usuarios, Productos, Inventario, Compras) segun el
 * tipo de cuenta logueada (Usuario comun o Administrador).
 * ============================================================================
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String homeUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO_LOGUEADO");
        model.addAttribute("usuario", usuario);
        return "home";
    }

    @GetMapping("/admin/home")
    public String homeAdministrador(HttpSession session, Model model) {
        Administrador administrador = (Administrador) session.getAttribute("ADMIN_LOGUEADO");
        model.addAttribute("administrador", administrador);
        return "admin-home";
    }
}
