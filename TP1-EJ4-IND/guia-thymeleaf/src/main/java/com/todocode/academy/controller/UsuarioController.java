package com.todocode.academy.controller;

import com.todocode.academy.model.Usuario;
import com.todocode.academy.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        model.addAttribute("mensaje", "¡Bienvenido al Curso de Thymeleaf de TodoCode Academy!");
        model.addAttribute("curso", "Spring Boot + Thymeleaf");
        return "index";
    }

    @GetMapping("/detalle")
    public String detalle(@RequestParam(required = false) Long id, Model model) {
        if (id != null)
            usuarioService.buscarPorId(id).ifPresent(u -> model.addAttribute("usuario", u));
        return "detalle";
    }

    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("usuarioForm", new Usuario());
        return "usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, HttpSession session) {
        usuarioService.guardar(usuario);
        session.setAttribute("mensajeExito", "Guardado correctamente.");
        return "redirect:/usuarios/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session) {
        usuarioService.eliminar(id);
        session.setAttribute("mensajeExito", "Eliminado correctamente.");
        return "redirect:/usuarios/lista";
    }
}