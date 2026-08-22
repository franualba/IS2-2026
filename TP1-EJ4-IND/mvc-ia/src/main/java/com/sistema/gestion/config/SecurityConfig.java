package com.sistema.gestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ============================================================================
 * CONFIGURACION - SecurityConfig
 * ============================================================================
 * Se define unicamente el bean PasswordEncoder (algoritmo BCrypt) que usan
 * los Services para encriptar y verificar contrasenas.
 *
 * NOTA DE DISENO: intencionalmente NO se habilita spring-boot-starter-security
 * completo (con su filtro de autenticacion HttpSecurity) para poder
 * implementar el flujo de login EXACTAMENTE como lo modela el UML: los
 * metodos Usuario.iniciarSesion(password) / Administrador.iniciarSesion(password)
 * y el manejo manual de intentos/bloqueo en el AuthController usando la
 * sesion HTTP (HttpSession). Se conserva de todas formas el hashing seguro
 * de contrasenas (BCrypt) como buena practica minima indispensable.
 * ============================================================================
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
