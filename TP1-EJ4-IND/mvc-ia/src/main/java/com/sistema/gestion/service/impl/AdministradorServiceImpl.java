package com.sistema.gestion.service.impl;

import com.sistema.gestion.exception.CorreoYaRegistradoException;
import com.sistema.gestion.model.Administrador;
import com.sistema.gestion.repository.AdministradorRepository;
import com.sistema.gestion.service.AdministradorService;
import com.sistema.gestion.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * SERVICIO (IMPLEMENTACION) - AdministradorServiceImpl
 * ============================================================================
 */
@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    public AdministradorServiceImpl(AdministradorRepository administradorRepository,
                                     PasswordEncoder passwordEncoder,
                                     UsuarioService usuarioService) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    @Override
    @Transactional
    public Administrador registrar(Administrador administrador) {
        if (administradorRepository.existsByCorreo(administrador.getCorreo())) {
            throw new CorreoYaRegistradoException(
                    "Ya existe un administrador registrado con el correo " + administrador.getCorreo());
        }
        administrador.setPassword(passwordEncoder.encode(administrador.getPassword()));
        return administradorRepository.save(administrador);
    }

    @Override
    public Optional<Administrador> iniciarSesion(String correo, String passwordPlano) {
        Optional<Administrador> adminOpt = administradorRepository.findByCorreo(correo);
        if (adminOpt.isEmpty()) {
            return Optional.empty();
        }
        Administrador administrador = adminOpt.get();
        boolean passwordCorrecta = passwordEncoder.matches(passwordPlano, administrador.getPassword());
        // Se respeta el metodo de dominio Administrador.iniciarSesion(boolean)
        boolean loginExitoso = administrador.iniciarSesion(passwordCorrecta);
        return loginExitoso ? Optional.of(administrador) : Optional.empty();
    }

    @Override
    public Optional<Administrador> buscarPorCorreo(String correo) {
        return administradorRepository.findByCorreo(correo);
    }

    @Override
    public List<Administrador> listarTodos() {
        return administradorRepository.findAll();
    }

    @Override
    @Transactional
    public void desbloquearUsuario(Long usuarioId) {
        // gestionarUsuario() / desbloquearUsuario(): el Administrador delega
        // en UsuarioService la operacion concreta sobre el Usuario.
        usuarioService.desbloquear(usuarioId);
    }
}
