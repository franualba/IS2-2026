package com.colegio.servicio;

import com.colegio.modelo.Profesor;
import com.colegio.repositorio.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  PUENTE ENTRE NUESTRA BD Y SPRING SECURITY (autenticación)
 * ═══════════════════════════════════════════════════════════════════
 *
 * Spring Security llama a loadUserByUsername(username) durante el login,
 * pasando el valor del campo "username" (el CORREO del docente).
 *
 * Flujo completo de autenticación:
 *  1. El formulario POST /login llega al filtro UsernamePasswordAuthenticationFilter.
 *  2. El filtro invoca a este UserDetailsService.
 *  3. Aquí buscamos el Profesor por su correo y devolvemos un objeto UserDetails
 *     con: usuario, hash de password, estado (habilitado/no eliminado) y ROLES.
 *  4. Spring Security compara el hash guardado con lo escrito en el formulario
 *     usando el PasswordEncoder (BCrypt). Si coincide → sesión autenticada.
 *
 * Detalles de seguridad importantes:
 *  - Si el usuario no existe lanzamos UsernameNotFoundException (Spring muestra
 *    un mensaje genérico, no revelando si el correo existe o no → evita
 *    enumeración de usuarios).
 *  - .disabled(...) / .accountLocked(...) mapean "habilitado" y "eliminado"
 *    para bloquear cuentas sin borrar datos.
 */
@Service
@RequiredArgsConstructor
public class UsuarioDetallesServicio implements UserDetailsService {

    private final ProfesorRepository profesorRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Profesor profesor = profesorRepository.findByUsuario(Objects.requireNonNull(correo, "correo no puede ser null"))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + correo));

        // Convertimos los roles de la BD (ROLE_PROFESOR...) a autoridades de Spring.
        List<GrantedAuthority> autoridades = profesor.getRoles().stream()
                .map(rol -> (GrantedAuthority) new SimpleGrantedAuthority(rol.getNombre()))
                .toList();

        // User de Spring Security: usuario, hash, estado y autoridades.
        return User.withUsername(Objects.requireNonNull(profesor.getUsuario(), "usuario no puede ser null"))
                .password(Objects.requireNonNull(profesor.getPassword(), "password no puede ser null")) // SIEMPRE el hash, nunca crudo
                .authorities(autoridades)
                .disabled(!profesor.isHabilitado())        // cuenta deshabilitada → login rechazado
                .accountLocked(profesor.isEliminado())     // eliminación lógica bloquea el acceso
                .build();
    }
}