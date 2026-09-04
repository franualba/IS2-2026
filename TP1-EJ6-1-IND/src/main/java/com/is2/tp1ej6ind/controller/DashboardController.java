package com.is2.tp1ej6ind.controller;

import com.is2.tp1ej6ind.model.Profesor;
import com.is2.tp1ej6ind.service.ProfesorService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la vista principal del sistema.
 *
 * Muestra el panel del docente y permite usarse como punto de entrada una vez autenticado.
 */
@Controller
public class DashboardController {

    private final ProfesorService profesorService;

    public DashboardController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        Profesor profesor = profesorService.buscarPorEmail(authentication.getName());
        model.addAttribute("profesor", profesor);
        return "dashboard";
    }
}
