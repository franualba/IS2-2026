package com.colegio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  CONFIGURACIÓN CENTRAL DE SEGURIDAD (Spring Security 6)
 * ═══════════════════════════════════════════════════════════════════
 *
 * @EnableWebSecurity: habilita el filtro de seguridad web e integración con MVC.
 *
 * 1) PASSWORD ENCODER (BCrypt):
 *    - BCrypt es un hash "con sal" y adaptable: cada contraseña se guarda con
 *      una sal aleatoria distinta; el "costo" (por defecto 10) puede subirse
 *      con el tiempo sin romper hashes viejos.
 *    - NUNCA usar MD5/SHA1 ni texto plano. Con BCrypt la comparación se hace
 *      mediante passwordEncoder.matches(crudo, hash), nunca con equals().
 *
 * 2) SECURITY FILTER CHAIN (cadena de filtros):
 *    - CSRF activado por defecto: protege los formularios POST contra ataques
 *      Cross-Site Request Forgery. Thymeleaf con th:action agrega el token
 *      oculto (_csrf) AUTOMÁTICAMENTE en todos los <form>. Por eso el botón de
 *      "Salir" y los formularios usan POST.
 *    - authorizeHttpRequests: reglas de autorización por URL y por rol.
 *    - formLogin: página de login propia (/login) en lugar del login por defecto.
 *    - logout: invalida sesión y borra la cookie JSESSIONID.
 *    - sessionManagement: limita sesiones concurrentes (un docente no puede
 *      estar logueado dos veces a la vez; mitiga robo de sesión).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean codificador de contraseñas. Lo usan:
     *  - ProfesorServicio al REGISTRAR y al CAMBIAR contraseña (para hashear).
     *  - El mecanismo de autenticación de Spring Security (para verificar).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // costo por defecto (10 rondas)
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ── AUTORIZACIÓN DE REQUESTS ─────────────────────────────────────
            .authorizeHttpRequests(auth -> auth
                // Públicas: páginas de acceso y recursos estáticos (CSS/JS de Bootstrap).
                .requestMatchers("/registro", "/login",
                                 "/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                // Zona de administración: requiere rol ADMIN.
                // hasRole("ADMIN") compara contra autoridad "ROLE_ADMIN".
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Todo lo demás exige autenticación (docente logueado).
                .anyRequest().authenticated()
            )
            // ── FORMULARIO DE LOGIN ──────────────────────────────────────────
            .formLogin(form -> form
                .loginPage("/login")                      // página propia (Thymeleaf)
                .loginProcessingUrl("/login")             // URL que procesa Spring Security
                .usernameParameter("username")            // name del input correo
                .passwordParameter("password")            // name del input contraseña
                .defaultSuccessUrl("/inicio", false)      // destino tras login OK
                .failureUrl("/login?error=true")          // destino tras login fallido
                .permitAll()
            )
            // ── CIERRE DE SESIÓN ─────────────────────────────────────────────
            .logout(logout -> logout
                .logoutUrl("/logout")                     // debe invocarse por POST (CSRF)
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)              // destruye la sesión
                .deleteCookies("JSESSIONID")              // elimina cookie de sesión
                .permitAll()
            )
            // ── CONTROL DE SESIONES (hardening) ──────────────────────────────
            .sessionManagement(sm -> sm
                .maximumSessions(1)                       // 1 sesión simultánea por usuario
            );

        return http.build();
    }
}