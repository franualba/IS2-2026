package ar.edu.club.web;

import ar.edu.club.domain.MedioPago;
import ar.edu.club.dto.*;
import ar.edu.club.repository.GrupoFamiliarRepository;
import ar.edu.club.service.PagoCuotaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/** Controlador MVC: recibe formularios, delega la transacción al servicio y selecciona vistas Thymeleaf. */
@Controller
public class ClubController {
    private final GrupoFamiliarRepository grupos;
    private final PagoCuotaService pagos;
    public ClubController(GrupoFamiliarRepository grupos, PagoCuotaService pagos) { this.grupos = grupos; this.pagos = pagos; }
    @GetMapping("/login") String login() { return "login"; }
    @GetMapping({"/", "/familias"}) String familias(Model model) {
        model.addAttribute("familias", grupos.findAll().stream().map(GrupoFamiliarDto::from).toList());
        return "familias";
    }
    @GetMapping("/familias/{id}/pagos/nuevo") String nuevoPago(@PathVariable String id, Model model) {
        model.addAttribute("pago", new PagoCuotaRequest(id, "", null, null));
        model.addAttribute("medios", MedioPago.values());
        return "pago-form";
    }
    @PostMapping("/familias/pagos") String registrarPago(@Valid @ModelAttribute("pago") PagoCuotaRequest pago, BindingResult result, Model model) {
        if (result.hasErrors()) { model.addAttribute("medios", MedioPago.values()); return "pago-form"; }
        try { pagos.registrar(pago); return "redirect:/familias?ok"; }
        catch (RuntimeException error) { result.reject("pago.invalido", error.getMessage()); model.addAttribute("medios", MedioPago.values()); return "pago-form"; }
    }
}
