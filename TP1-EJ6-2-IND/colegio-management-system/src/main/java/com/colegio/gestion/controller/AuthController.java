package com.colegio.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("mensajeError", "Email o contraseña incorrectos.");
        }

        if (logout != null) {
            model.addAttribute("mensajeLogout", "Sesión cerrada correctamente.");
        }

        return "login";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }
}
