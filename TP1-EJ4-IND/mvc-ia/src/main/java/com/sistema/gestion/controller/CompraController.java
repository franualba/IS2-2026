package com.sistema.gestion.controller;

import com.sistema.gestion.model.Compra;
import com.sistema.gestion.model.DetalleCompra;
import com.sistema.gestion.model.Producto;
import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.service.CompraService;
import com.sistema.gestion.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================================
 * CONTROLADOR (CAPA C de MVC) - CompraController
 * ============================================================================
 * Permite al Usuario logueado (en sesion) registrar una compra simple
 * (un producto + cantidad, a modo de ejemplo funcional del flujo completo
 * Compra -> DetalleCompra -> disminuirInventario()) y anularla.
 * ============================================================================
 */
@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final ProductoService productoService;

    public CompraController(CompraService compraService, ProductoService productoService) {
        this.compraService = compraService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("USUARIO_LOGUEADO");
        model.addAttribute("compras", compraService.listarPorUsuario(usuario));
        return "compras/list";
    }

    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "compras/form";
    }

    @PostMapping
    public String registrar(@RequestParam Long productoId,
                             @RequestParam int cantidad,
                             HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("USUARIO_LOGUEADO");
        Producto producto = productoService.buscarPorId(productoId);

        DetalleCompra detalle = new DetalleCompra();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        // Precio unitario de ejemplo: en un caso real vendria de una lista de
        // precios propia del Producto; se deja como campo editable del detalle.
        detalle.setPrecioUnitario(0.0);

        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.getDetalles().add(detalle);

        compraService.registrarCompra(compra);
        return "redirect:/compras";
    }

    @PostMapping("/{id}/anular")
    public String anular(@PathVariable Long id) {
        compraService.anularCompra(id);
        return "redirect:/compras";
    }
}
