package com.colegio.gestion.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.colegio.gestion.entity.Profesor;
import com.colegio.gestion.repository.ProfesorRepository;

/**
 * SERVICIO: CustomUserDetailsService
 * 
 * DESCRIPCIÓN:
 * Implementación personalizada de UserDetailsService para Spring Security.
 * Este servicio es el puente entre Spring Security y nuestra base de datos
 * de profesores. Se encarga de cargar los datos del usuario para autenticación.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Service: Marca esta clase como componente de servicio.
 *   Spring la detecta automáticamente y la registra como bean.
 * - @Transactional(readOnly = true): Todas las operaciones son de solo lectura.
 *   Mejora el performance al no necesitar transacciones de escritura.
 * 
 * INTERFAZ UserDetailsService:
 * Spring Security requiere implementar un único método:
 * - loadUserByUsername(String username): Carga datos del usuario
 * 
 * FLUJO DE AUTENTICACIÓN:
 * 1. Usuario envía email/password en formulario de login
 * 2. Spring Security llama a loadUserByUsername(email)
 * 3. Este servicio busca el profesor en la BD por email
 * 4. Si existe, crea UserDetails con password y roles
 * 5. Spring compara password usando BCrypt
 * 6. Si coincide, crea sesión autenticada
 * 
 * SEGURIDAD:
 * - El password NUNCA se expone en logs o respuestas
 * - Los roles se asignan según el tipo de usuario
 * - Se valida que el usuario esté verificado (opcional)
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * REPOSITORIO: ProfesorRepository
     * Para buscar profesores por email desde la base de datos.
     */
    private final ProfesorRepository profesorRepository;

    /**
     * CONSTRUCTOR: Inyección de dependencias
     * @param profesorRepository Repositorio de profesores
     */
    public CustomUserDetailsService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    /**
     * MÉTODO: loadUserByUsername
     * DESCRIPCIÓN: Carga los datos del usuario para autenticación.
     * Método requerido por la interfaz UserDetailsService.
     * 
     * PROCESO:
     * 1. Busca el profesor por email (username) en la BD
     * 2. Si no existe, lanza UsernameNotFoundException
     * 3. Si está eliminado, lanza excepción (no puede login)
     * 4. Crea objeto UserDetails con credenciales y authorities
     * 
     * PARÁMETROS DE User.builder():
     * - username: Email del profesor (identificador único)
     * - password: Password encriptado con BCrypt (desde BD)
     * - authorities: Lista de roles/permisos (ROLE_DOCENTE)
     * - accountExpired: false (cuenta no expirada)
     * - accountLocked: false (cuenta no bloqueada)
     * - credentialsExpired: false (credenciales no expiradas)
     * - disabled: !profesor.isVerificado() (deshabilitado si no verificado)
     * 
     * @param username Email del usuario (proporcionado en login form)
     * @return UserDetails Objeto con información de autenticación
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar profesor por email en la base de datos
        Optional<Profesor> profesorOpt = profesorRepository.findByEmail(username);

        // Si no existe el profesor, lanzar excepción
        if (profesorOpt.isEmpty()) {
            throw new UsernameNotFoundException(
                "No se encontró un usuario con el email: " + username
            );
        }

        Profesor profesor = profesorOpt.get();

        // Verificar que el profesor no esté eliminado (soft delete)
        if (profesor.isEliminado()) {
            throw new UsernameNotFoundException(
                "La cuenta ha sido desactivada. Contacte al administrador."
            );
        }

        // Asignar rol por defecto ROLE_DOCENTE a todos los profesores
        // Se pueden agregar más roles según necesidades (ROLE_ADMIN, etc.)
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_DOCENTE"));

        // Opcional: Agregar rol ADMIN si es necesario
        // if (profesor.esAdmin()) {
        //     authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        // }

        // Construir objeto UserDetails para Spring Security
        return User.builder()
            .username(profesor.getEmail())           // Username para login
            .password(profesor.getPassword())        // Password encriptado (BCrypt)
            .authorities(authorities)                // Roles/permisos
            .accountExpired(false)                   // Cuenta no expirada
            .accountLocked(false)                    // Cuenta no bloqueada
            .credentialsExpired(false)               // Credenciales no expiradas
            .disabled(!profesor.isVerificado())      // Deshabilitado si no verificado
            .build();
    }
}
