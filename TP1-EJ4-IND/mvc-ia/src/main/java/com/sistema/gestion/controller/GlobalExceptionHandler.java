package com.sistema.gestion.controller;

import com.sistema.gestion.exception.CorreoYaRegistradoException;
import com.sistema.gestion.exception.UsuarioNoEncontradoException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

/**
 * ============================================================================
 * CONTROLADOR - GlobalExceptionHandler
 * ============================================================================
 * @ControllerAdvice intercepta excepciones lanzadas desde CUALQUIER
 * Controller de la aplicacion y las traduce a una vista de error amigable,
 * evitando que el usuario final vea un stacktrace crudo. Forma parte de la
 * capa Controlador en un sentido amplio (maneja el flujo de errores HTTP).
 * ============================================================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UsuarioNoEncontradoException.class, NoSuchElementException.class})
    public String manejarNoEncontrado(RuntimeException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public String manejarCorreoDuplicado(CorreoYaRegistradoException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error";
    }
}
