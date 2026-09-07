package com.colegio.servicio;

import com.colegio.dto.CambioPasswordDTO;
import com.colegio.dto.ProfesorDTO;
import com.colegio.dto.ProfesorRegistroDTO;
import com.colegio.modelo.Profesor;
import com.colegio.modelo.Rol;
import com.colegio.repositorio.ProfesorRepository;
import com.colegio.repositorio.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * SERVICIO DE PROFESORES: concentra la lógica de negocio del diagrama
 * (registrarProfesor, editarProfesor, eliminarProfesor) más la lógica de
 * SEGURIDAD (registro con hash, cambio de contraseña, correo de bienvenida).
 *
 * @Service: estereotipo de Spring (Bean de capa de negocio).
 * @RequiredArgsConstructor (Lombok): genera constructor con los campos final →
 *   inyección de dependencias por constructor (recomendada: inmutabilidad y
 *   facilidad de testing).
 * @Transactional: delimita la transacción de BD; si algo falla, hace rollback.
 */
@Service
@RequiredArgsConstructor
public class ProfesorServicio {

    private final ProfesorRepository profesorRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;   // Bean BCrypt de SecurityConfig
    private final EmailServicio emailServicio;

    /**
     * REGISTRO DE DOCENTE (requisito del enunciado).
     * Pasos de seguridad:
     *  1. Validar que el correo no exista (evita cuentas duplicadas).
     *  2. Hashear la contraseña con BCrypt ANTES de persistir.
     *  3. Asignar el rol ROLE_PROFESOR (autorización por defecto).
     *  4. Enviar correo de bienvenida al correo personal.
     */
    @Transactional
    public void registrar(ProfesorRegistroDTO dto) {
        Objects.requireNonNull(dto, "dto no puede ser null");
        if (profesorRepository.existsByUsuario(dto.getCorreo())) {
            throw new IllegalStateException("Ya existe una cuenta con el correo " + dto.getCorreo());
        }

        Profesor profesor = new Profesor();
        profesor.setNombre(dto.getNombre());
        profesor.setApellido(dto.getApellido());
        profesor.setSexo(dto.getSexo());
        profesor.setFechaNacimiento(dto.getFechaNacimiento());
        profesor.setEspecialidad(dto.getEspecialidad());
        profesor.setUsuario(dto.getCorreo());                       // usuario = correo personal
        profesor.setPassword(passwordEncoder.encode(
            Objects.requireNonNull(dto.getPassword(), "password no puede ser null"))); // hash BCrypt
        profesor.setHabilitado(true);
        profesor.setEliminado(false);

        Rol rolProfesor = rolRepository.findByNombre("ROLE_PROFESOR")
                .orElseThrow(() -> new IllegalStateException("Rol ROLE_PROFESOR no inicializado"));
        profesor.getRoles().add(rolProfesor);

        profesorRepository.save(profesor);

        // Correo de bienvenida (asíncrono, no rompe la transacción).
        emailServicio.enviarBienvenida(profesor);
    }

    /**
     * CAMBIO DE CONTRASEÑA (requisito del enunciado).
     * Seguridad:
     *  - Se verifica la contraseña ACTUAL con matches() (comparación de hashes).
     *  - La nueva se guarda hasheada.
     *  - @Transactional garantiza atomicidad.
     */
    @Transactional
    public void cambiarPassword(String usuario, CambioPasswordDTO dto) {
        Objects.requireNonNull(dto, "dto no puede ser null");
        Profesor profesor = profesorRepository.findByUsuario(Objects.requireNonNull(usuario, "usuario no puede ser null"))
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        // Verificación de identidad: compara texto plano ingresado vs hash almacenado.
        if (!passwordEncoder.matches(Objects.requireNonNull(dto.getPasswordActual(), "passwordActual no puede ser null"),
            Objects.requireNonNull(profesor.getPassword(), "La contraseña almacenada no puede ser null"))) {
            throw new IllegalStateException("La contraseña actual no es correcta");
        }

        profesor.setPassword(passwordEncoder.encode(
            Objects.requireNonNull(dto.getPasswordNueva(), "passwordNueva no puede ser null")));
        profesorRepository.save(profesor); // la auditoría registra modificadoPor/fecha
    }

    /** Listado de profesores activos (solo datos públicos, vía DTO). */
    @Transactional(readOnly = true)
    public List<ProfesorDTO> listar() {
        return profesorRepository.findByEliminadoFalse().stream().map(p -> {
            ProfesorDTO dto = new ProfesorDTO();
            dto.setIdProfesor(p.getIdProfesor());
            dto.setNombre(p.getNombre());
            dto.setApellido(p.getApellido());
            dto.setSexo(String.valueOf(p.getSexo()));
            dto.setEspecialidad(p.getEspecialidad());
            dto.setUsuario(p.getUsuario());
            return dto;
        }).toList();
    }

    /** eliminarProfesor del diagrama, como ELIMINACIÓN LÓGICA. */
    @Transactional
    public void eliminarLogico(Long id) {
        profesorRepository.findById(Objects.requireNonNull(id, "id no puede ser null")).ifPresent(p -> {
            p.setEliminado(true);      // no se borra: se desactiva y bloquea login
            profesorRepository.save(p);
        });
    }

    public long contar() {
        return profesorRepository.findByEliminadoFalse().size();
    }
}