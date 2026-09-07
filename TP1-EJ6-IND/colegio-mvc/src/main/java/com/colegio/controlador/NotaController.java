package com.colegio.controlador;

import com.colegio.dto.NotaDTO;
import com.colegio.repositorio.AlumnoRepository;
import com.colegio.repositorio.MateriaRepository;
import com.colegio.servicio.NotaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/** CONTROLADOR CRUD DE NOTAS (expone listarNotas del diagrama). */
@Controller
@RequestMapping("/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaServicio notaServicio;
    private final AlumnoRepository alumnoRepository;
    private final MateriaRepository materiaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("notas", notaServicio.listar());
        return "notas/lista";
    }

    /** Listado de notas de un alumno específico (listarNotas del diagrama). */
    @GetMapping("/alumno/{id}")
    public String listarPorAlumno(@PathVariable Long id, Model model) {
        model.addAttribute("notas", notaServicio.listarNotasPorAlumno(id));
        return "notas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("dto", new NotaDTO());
        cargarListas(model);
        return "notas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("dto") NotaDTO dto,
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            cargarListas(model);
            return "notas/formulario";
        }
        notaServicio.guardar(dto);
        return "redirect:/notas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        notaServicio.eliminar(id);
        return "redirect:/notas";
    }

    private void cargarListas(Model model) {
        model.addAttribute("alumnos", alumnoRepository.findByEliminadoFalse());
        model.addAttribute("materias", materiaRepository.findByEliminadoFalse());
    }
}