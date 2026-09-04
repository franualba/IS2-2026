package com.colegio.controlador;

import com.colegio.dto.AlumnoDTO;
import com.colegio.repositorio.AulaRepository;
import com.colegio.servicio.AlumnoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLADOR CRUD DE ALUMNOS (requiere sesión docente).
 * Solo trabaja con AlumnoDTO; las listas auxiliares (aulas) se pasan para
 * rellenar los <select> del formulario.
 */
@Controller
@RequestMapping("/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoServicio alumnoServicio;
    private final AulaRepository aulaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alumnos", alumnoServicio.listar()); // List<AlumnoDTO>
        return "alumnos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("dto", new AlumnoDTO());
        model.addAttribute("aulas", aulaRepository.findAll());
        return "alumnos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("dto") AlumnoDTO dto,
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("aulas", aulaRepository.findAll());
            return "alumnos/formulario";
        }
        alumnoServicio.guardar(dto);
        return "redirect:/alumnos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("dto", alumnoServicio.buscar(id));
        model.addAttribute("aulas", aulaRepository.findAll());
        return "alumnos/formulario";
    }

    /** Eliminación lógica (patrón del diagrama: eliminarAlumno). */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        alumnoServicio.eliminarLogico(id);
        return "redirect:/alumnos";
    }
}