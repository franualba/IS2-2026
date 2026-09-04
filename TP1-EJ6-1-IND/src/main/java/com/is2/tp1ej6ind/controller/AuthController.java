package com.is2.tp1ej6ind.controller;

import com.is2.tp1ej6ind.dto.CambioPasswordDTO;
import com.is2.tp1ej6ind.dto.ProfesorRegistroDTO;
import com.is2.tp1ej6ind.model.Profesor;
import com.is2.tp1ej6ind.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controlador responsable del registro, login y cambio de contraseña del docente.
 *
 * Este componente pertenece a la capa MVC de presentación y interactúa con los servicios
 * utilizando DTO para evitar acoplar la vista directamente con entidades JPA.
 */
@Controller
public class AuthController {

    private final ProfesorService profesorService;

    public AuthController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("profesor", new ProfesorRegistroDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("profesor") ProfesorRegistroDTO dto,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            Profesor registrado = profesorService.registrarProfesor(dto);
            model.addAttribute("successMessage", "Docente registrado correctamente. Se envió un correo de bienvenida a " + registrado.getEmail());
            model.addAttribute("profesor", new ProfesorRegistroDTO());
            return "login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profesor profesor = profesorService.buscarPorEmail(authentication.getName());
        model.addAttribute("profesor", profesor);
        return "dashboard";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("cambioPasswordDTO", new CambioPasswordDTO());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("cambioPasswordDTO") CambioPasswordDTO dto,
                                BindingResult result,
                                Model model,
                                Authentication authentication) {
        if (result.hasErrors()) {
            return "change-password";
        }

        try {
            profesorService.cambiarPassword(authentication.getName(), dto);
            model.addAttribute("successMessage", "Contraseña actualizada correctamente.");
            model.addAttribute("cambioPasswordDTO", new CambioPasswordDTO());
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "change-password";
    }
}
