package com.is2.tp1ej6ind.service;

import com.is2.tp1ej6ind.dto.CambioPasswordDTO;
import com.is2.tp1ej6ind.dto.ProfesorDTO;
import com.is2.tp1ej6ind.dto.ProfesorRegistroDTO;
import com.is2.tp1ej6ind.model.Profesor;
import com.is2.tp1ej6ind.repository.ProfesorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio principal del docente.
 *
 * Contiene la lógica de negocio del flujo de autenticación, registro y cambio de contraseña.
 * Además se encarga de mapear entidades a DTO para respetar la arquitectura MVC con transferencia
 * de datos por DTO entre capas.
 */
@Service
public class ProfesorService implements UserDetailsService {

    private final ProfesorRepository profesorRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ProfesorService(ProfesorRepository profesorRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.profesorRepository = profesorRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profesor profesor = profesorRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("No existe un docente con ese correo"));

        return User.withUsername(profesor.getEmail())
            .password(profesor.getPassword())
            .roles("DOCENTE")
            .build();
    }

    @Transactional
    public Profesor registrarProfesor(ProfesorRegistroDTO dto) {
        if (profesorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un docente con ese correo");
        }

        Profesor profesor = new Profesor();
        profesor.setNombre(dto.getNombre());
        profesor.setApellido(dto.getApellido());
        profesor.setSexo(dto.getSexo());
        profesor.setFechaNacimiento(dto.getFechaNacimiento());
        profesor.setEmail(dto.getEmail().trim().toLowerCase());
        profesor.setPassword(passwordEncoder.encode(dto.getPassword()));
        profesor.setEnabled(true);
        profesor.setRole("ROLE_DOCENTE");

        Profesor guardado = profesorRepository.save(profesor);
        emailService.sendWelcomeEmail(guardado);
        return guardado;
    }

    @Transactional(readOnly = true)
    public ProfesorDTO obtenerPerfil(String email) {
        Profesor profesor = profesorRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Docente no encontrado"));
        return mapToDto(profesor);
    }

    @Transactional(readOnly = true)
    public List<ProfesorDTO> listarProfesores() {
        return profesorRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public void cambiarPassword(String email, CambioPasswordDTO dto) {
        Profesor profesor = profesorRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Docente no encontrado"));

        if (!passwordEncoder.matches(dto.getPasswordActual(), profesor.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        if (!dto.getNuevaPassword().equals(dto.getConfirmacion())) {
            throw new IllegalArgumentException("La nueva contraseña y la confirmación no coinciden");
        }

        profesor.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
        profesorRepository.save(profesor);
    }

    @Transactional(readOnly = true)
    public Profesor buscarPorEmail(String email) {
        return profesorRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Docente no encontrado"));
    }

    private ProfesorDTO mapToDto(Profesor profesor) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(profesor.getId());
        dto.setNombre(profesor.getNombre());
        dto.setApellido(profesor.getApellido());
        dto.setSexo(profesor.getSexo());
        dto.setFechaNacimiento(profesor.getFechaNacimiento());
        dto.setEmail(profesor.getEmail());
        dto.setRole(profesor.getRole());
        return dto;
    }
}
