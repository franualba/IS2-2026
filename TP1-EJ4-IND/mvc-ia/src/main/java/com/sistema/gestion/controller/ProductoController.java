package com.sistema.gestion.controller;

import com.sistema.gestion.model.Producto;
import com.sistema.gestion.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * CONTROLADOR (CAPA C de MVC) - ProductoController
 * ============================================================================
 * Expone el CRUD de Producto (registrarProducto / editarProducto /
 * eliminarProducto del UML) y el stock actual calculado a partir de
 * Inventario.
 * ============================================================================
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        var productos = productoService.listarTodos();
        Map<Long, Integer> stockPorProducto = new HashMap<>();
        for (Producto p : productos) {
            stockPorProducto.put(p.getId(), productoService.calcularStockActual(p.getId()));
        }
        model.addAttribute("productos", productos);
        model.addAttribute("stockPorProducto", stockPorProducto);
        return "productos/list";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/form";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("producto") Producto producto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "productos/form";
        }
        // +registrarProducto(): void (UML)
        productoService.registrarProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.buscarPorId(id));
        return "productos/form";
    }

    @PostMapping("/{id}")
    public String editar(@PathVariable Long id,
                          @Valid @ModelAttribute("producto") Producto producto,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "productos/form";
        }
        // +editarProducto(): void (UML)
        productoService.editarProducto(id, producto);
        return "redirect:/productos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        // +eliminarProducto(): void (UML)
        productoService.eliminarProducto(id);
        return "redirect:/productos";
    }
}
