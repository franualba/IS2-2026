package com.sistema.gestion.controller;

import com.sistema.gestion.model.Inventario;
import com.sistema.gestion.service.InventarioService;
import com.sistema.gestion.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================================
 * CONTROLADOR (CAPA C de MVC) - InventarioController
 * ============================================================================
 * Permite registrar movimientos manuales de stock (registrarMovimiento()
 * del UML), tipicamente ENTRADAs de mercaderia; las SALIDAs se generan
 * automaticamente al registrar una Compra (ver CompraServiceImpl).
 * ============================================================================
 */
@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;

    public InventarioController(InventarioService inventarioService, ProductoService productoService) {
        this.inventarioService = inventarioService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("movimientos", inventarioService.listarTodos());
        return "inventario/list";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("movimiento", new Inventario());
        model.addAttribute("productos", productoService.listarTodos());
        return "inventario/form";
    }

    @PostMapping
    public String registrar(@ModelAttribute("movimiento") Inventario movimiento,
                             @RequestParam Long productoId) {
        movimiento.setProducto(productoService.buscarPorId(productoId));
        // +registrarMovimiento(): void (UML)
        inventarioService.registrarMovimiento(movimiento);
        return "redirect:/inventario";
    }
}
