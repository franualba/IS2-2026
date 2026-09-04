package com.colegio.controlador;

import com.colegio.servicio.AlumnoServicio;
import com.colegio.servicio.MateriaServicio;
import com.colegio.servicio.ProfesorServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLADOR DEL PANEL PRINCIPAL (dashboard post-login).
 * "/" redirige a "/inicio" (ruta protegida por SecurityConfig).
 */
@Controller
@RequiredArgsConstructor
public class InicioController {

    private final ProfesorServicio profesorServicio;
    private final AlumnoServicio alumnoServicio;
    private final MateriaServicio materiaServicio;

    @GetMapping("/")
    public String raiz() {
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {
        model.addAttribute("totalProfesores", profesorServicio.contar());
        model.addAttribute("totalAlumnos", alumnoServicio.contar());
        model.addAttribute("totalMaterias", materiaServicio.contar());
        return "inicio";
    }
}