package com.colegio.controlador;

import com.colegio.dto.CambioPasswordDTO;
import com.colegio.dto.ProfesorRegistroDTO;
import com.colegio.servicio.ProfesorServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * CONTROLADOR DE AUTENTICACIÓN (capa Controlador del MVC).
 *
 * Maneja: login (vista), registro de docentes, y cambio de contraseña.
 * El PROCESAMIENTO real del login lo hace Spring Security (filtro), este
 * controlador solo sirve la página y el formulario de registro/cambio.
 *
 * @Controller: marca la clase como controlador web; los String devueltos son
 *              nombres de vistas Thymeleaf (patrón vista-modelo).
 * @Valid + BindingResult: validación del DTO con jakarta.validation; los errores
 *              se renderizan en el formulario con th:errors (feedback al usuario
 *              sin ejecutar lógica inválida en el servicio).
 * RedirectAttributes: atributos "flash" que sobreviven a un redirect (POST→REDIRECT→GET,
 *              patrón PRG que evita re-envíos del formulario con F5).
 */
@Controller
@RequiredArgsConstructor
public class AutenticacionController {

    private final ProfesorServicio profesorServicio;

    /** Página de login (acceso público). */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** Formulario de registro docente (público). */
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("dto", new ProfesorRegistroDTO());
        return "registro";
    }

    /**
     * Procesamiento del registro.
     * Seguridad: validación de DTO, comparación de contraseñas y delegación al
     * servicio (que hashea y envía correo). Nunca se confía en datos del cliente.
     */
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("dto") ProfesorRegistroDTO dto,
                            BindingResult result,
                            RedirectAttributes redirect) {
        if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
            result.rejectValue("password", "error.dto", "Las contraseñas no coinciden");
        }
        if (result.hasErrors()) {
            return "registro"; // vuelve al formulario mostrando th:errors
        }
        try {
            profesorServicio.registrar(dto);
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
        return "redirect:/login?registrado=true";
    }

    /** Formulario de cambio de contraseña (requiere sesión). */
    @GetMapping("/cambio-password")
    public String mostrarCambio(Model model) {
        model.addAttribute("dto", new CambioPasswordDTO());
        return "cambio-password";
    }

    /**
     * Procesamiento del cambio de contraseña.
     * Principal lo inyecta Spring con el usuario autenticado de la sesión:
     * el cliente NO puede elegir sobre qué cuenta cambiar la contraseña
     * (previene escalamiento de privilegios horizontal).
     */
    @PostMapping("/cambio-password")
    public String cambiarPassword(@Valid @ModelAttribute("dto") CambioPasswordDTO dto,
                                  BindingResult result,
                                  Principal principal,
                                  RedirectAttributes redirect) {
        if (!dto.getPasswordNueva().equals(dto.getConfirmarPasswordNueva())) {
            result.rejectValue("passwordNueva", "error.dto", "Las contraseñas no coinciden");
        }
        if (result.hasErrors()) {
            return "cambio-password";
        }
        try {
            profesorServicio.cambiarPassword(principal.getName(), dto);
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/cambio-password";
        }
        redirect.addFlashAttribute("exito", "Contraseña actualizada correctamente");
        return "redirect:/cambio-password";
    }
}