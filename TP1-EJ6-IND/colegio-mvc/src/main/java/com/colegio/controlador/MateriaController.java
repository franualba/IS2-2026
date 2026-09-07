package com.colegio.controlador;

import com.colegio.dto.MateriaDTO;
import com.colegio.repositorio.ColegioRepository;
import com.colegio.servicio.MateriaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/** CONTROLADOR CRUD DE MATERIAS (misma arquitectura que AlumnoController). */
@Controller
@RequestMapping("/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaServicio materiaServicio;
    private final ColegioRepository colegioRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materias", materiaServicio.listar());
        return "materias/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("dto", new MateriaDTO());
        model.addAttribute("colegios", colegioRepository.findByEliminadoFalse());
        return "materias/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("dto") MateriaDTO dto,
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("colegios", colegioRepository.findByEliminadoFalse());
            return "materias/formulario";
        }
        materiaServicio.guardar(dto);
        return "redirect:/materias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("dto", materiaServicio.buscar(id));
        model.addAttribute("colegios", colegioRepository.findByEliminadoFalse());
        return "materias/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        materiaServicio.eliminarLogico(id);
        return "redirect:/materias";
    }
}