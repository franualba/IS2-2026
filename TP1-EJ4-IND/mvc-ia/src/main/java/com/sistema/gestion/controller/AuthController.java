package com.sistema.gestion.controller;

import com.sistema.gestion.dto.LoginForm;
import com.sistema.gestion.dto.RegistroUsuarioForm;
import com.sistema.gestion.exception.CorreoYaRegistradoException;
import com.sistema.gestion.model.Administrador;
import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.service.AdministradorService;
import com.sistema.gestion.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * ============================================================================
 * CONTROLADOR (CAPA C de MVC) - AuthController
 * ============================================================================
 * Concentra el flujo de autenticacion pedido en el enunciado:
 *
 *   1) La persona ingresa su correo y clave (GET/POST "/login").
 *   2) Si el correo NO esta registrado -> se lo redirige a "/registro"
 *      (con el correo pre-cargado) para que complete sus datos personales.
 *   3) Si el correo SI esta registrado pero la clave es incorrecta ->
 *      se incrementa el contador de intentos fallidos (Usuario.intentos).
 *   4) Al 3er intento fallido consecutivo -> el usuario queda BLOQUEADO
 *      (Usuario.estado = BLOQUEADO) y no puede volver a iniciar sesion
 *      hasta que un Administrador lo desbloquee.
 *   5) Si el login es correcto -> se crea la sesion HTTP (HttpSession) y se
 *      redirige al panel principal ("/home").
 *
 * El intento de login se prueba primero contra Usuario y, si no coincide,
 * contra Administrador (dos "tipos" de cuenta que comparten el correo como
 * identificador de login, tal como surge del UML donde ambos heredan de
 * Persona.correo).
 * ============================================================================
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;

    public AuthController(UsuarioService usuarioService, AdministradorService administradorService) {
        this.usuarioService = usuarioService;
        this.administradorService = administradorService;
    }

    /** Muestra el formulario de login (Vista: templates/login.html). */
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    /**
     * Procesa el intento de inicio de sesion.
     */
    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        String correo = loginForm.getCorreo().trim();

        // -----------------------------------------------------------------
        // PASO 1: si el correo no esta registrado en ninguna de las dos
        // tablas (Usuario / Administrador), se invita a la persona a
        // registrarse, tal como pide el enunciado.
        // -----------------------------------------------------------------
        boolean existeComoUsuario = usuarioService.existeCorreo(correo);
        boolean existeComoAdmin = administradorService.buscarPorCorreo(correo).isPresent();

        if (!existeComoUsuario && !existeComoAdmin) {
            model.addAttribute("correoNoRegistrado", true);
            model.addAttribute("correoSugerido", correo);
            return "login";
        }

        // -----------------------------------------------------------------
        // PASO 2: intenta primero como Administrador (sin limite de
        // intentos, segun UML), luego como Usuario (con control de 3
        // intentos y bloqueo).
        // -----------------------------------------------------------------
        if (existeComoAdmin) {
            Optional<Administrador> adminOpt =
                    administradorService.iniciarSesion(correo, loginForm.getPassword());
            if (adminOpt.isPresent()) {
                session.setAttribute("ADMIN_LOGUEADO", adminOpt.get());
                return "redirect:/admin/home";
            }
            model.addAttribute("credencialesInvalidas", true);
            return "login";
        }

        // existeComoUsuario == true
        Optional<Usuario> usuarioOpt = usuarioService.iniciarSesion(correo, loginForm.getPassword());

        if (usuarioOpt.isPresent()) {
            session.setAttribute("USUARIO_LOGUEADO", usuarioOpt.get());
            return "redirect:/home";
        }

        // Login fallido: se recupera el usuario para poder informar si
        // quedo bloqueado (ya persistido por UsuarioServiceImpl.iniciarSesion).
        Usuario usuarioActual = usuarioService.buscarPorCorreo(correo).orElse(null);
        if (usuarioActual != null && usuarioActual.estaBloqueado()) {
            model.addAttribute("usuarioBloqueado", true);
        } else {
            model.addAttribute("credencialesInvalidas", true);
            if (usuarioActual != null) {
                int restantes = Usuario.MAX_INTENTOS - usuarioActual.getIntentos();
                model.addAttribute("intentosRestantes", restantes);
            }
        }
        return "login";
    }

    /** Cierra la sesion actual (Usuario o Administrador) y vuelve al login. */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    /**
     * Muestra el formulario de registro de un nuevo Usuario. Si se llega
     * desde el login con un correo no registrado, se pre-carga ese correo.
     */
    @GetMapping("/registro")
    public String mostrarRegistro(@RequestParam(value = "correo", required = false) String correo,
                                   Model model) {
        RegistroUsuarioForm form = new RegistroUsuarioForm();
        if (correo != null && !correo.isBlank()) {
            form.setCorreo(correo);
        }
        model.addAttribute("registroForm", form);
        return "registro";
    }

    /**
     * Procesa el alta de un nuevo Usuario con sus datos personales
     * (Nombre, Apellido, Documento, Fecha de Nacimiento, Correo).
     */
    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("registroForm") RegistroUsuarioForm form,
                                    BindingResult bindingResult,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            return "registro";
        }

        if (usuarioService.existeCorreo(form.getCorreo())) {
            model.addAttribute("correoDuplicado", true);
            return "registro";
        }
        if (usuarioService.existeDocumento(form.getDocumento())) {
            model.addAttribute("documentoDuplicado", true);
            return "registro";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(form.getNombre());
        nuevoUsuario.setApellido(form.getApellido());
        nuevoUsuario.setDocumento(form.getDocumento());
        nuevoUsuario.setFechaDeNacimiento(form.getFechaDeNacimiento());
        nuevoUsuario.setCorreo(form.getCorreo());
        nuevoUsuario.setPassword(form.getPassword());

        try {
            usuarioService.registrar(nuevoUsuario);
        } catch (CorreoYaRegistradoException ex) {
            model.addAttribute("correoDuplicado", true);
            return "registro";
        }

        model.addAttribute("registroExitoso", true);
        return "login";
    }
}
