package com.colegio.controlador;

import com.colegio.servicio.ProfesorServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CONTROLADOR DE ADMINISTRACIÓN.
 * Seguridad: todas sus rutas viven bajo "/admin/**", que SecurityConfig protege
 * con hasRole("ADMIN"). Doble defensa: autorización por URL + controlador separado.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProfesorServicio profesorServicio;

    @GetMapping("/profesores")
    public String listarProfesores(Model model) {
        model.addAttribute("profesores", profesorServicio.listar()); // List<ProfesorDTO>
        return "admin/profesores";
    }

    /** Baja lógica de un docente (también bloquea su login). */
    @GetMapping("/profesores/eliminar/{id}")
    public String eliminarProfesor(@PathVariable Long id) {
        profesorServicio.eliminarLogico(id);
        return "redirect:/admin/profesores";
    }
}