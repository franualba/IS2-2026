package com.colegio.gestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.colegio.gestion.service.CustomUserDetailsService;

/**
 * CONFIGURACIÓN DE SEGURIDAD: SecurityConfig
 * 
 * DESCRIPCIÓN:
 * Clase de configuración para Spring Security que define:
 * - Políticas de autorización de URLs
 * - Proveedor de autenticación (DaoAuthenticationProvider)
 * - Codificador de contraseñas (BCrypt)
 * - Filtro de seguridad (SecurityFilterChain)
 * 
 * ANOTACIONES IMPORTANTES:
 * - @Configuration: Indica que esta clase contiene beans de configuración.
 * - @EnableWebSecurity: Habilita la seguridad web de Spring Security.
 * 
 * SEGURIDAD IMPLEMENTADA:
 * 1. Autenticación basada en formulario (formLogin)
 * 2. Encriptación de contraseñas con BCrypt (strength=10)
 * 3. Protección CSRF habilitada para formularios
 * 4. Roles de usuario para autorización
 * 5. Logout seguro con invalidación de sesión
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * SERVICIO: CustomUserDetailsService
     * Implementación personalizada para cargar usuarios desde la base de datos.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * CONSTRUCTOR: Inyección de dependencias
     * @param userDetailsService Servicio para cargar usuarios
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * BEAN: passwordEncoder
     * DESCRIPCIÓN: Codificador de contraseñas usando BCrypt.
     * BCrypt es un algoritmo de hashing adaptivo que incluye salt automático.
     * 
     * CARACTERÍSTICAS DE BCRIPT:
     * - Strength 10: Balance entre seguridad y performance (2^10 iteraciones)
     * - Salt automático: Previene ataques con rainbow tables
     * - Adaptable: Se puede aumentar strength según hardware
     * 
     * @return PasswordEncoder implementado con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt con strength=10 (1024 iteraciones)
        // Valores comunes: 10 (default), 12 (más seguro), 14 (máximo recomendado)
        return new BCryptPasswordEncoder(10);
    }

    /**
     * BEAN: authenticationProvider
     * DESCRIPCIÓN: Proveedor de autenticación que usa UserDetailsService.
     * DaoAuthenticationProvider autentica contra una base de datos.
     * 
     * FUNCIONAMIENTO:
     * 1. Recibe username/password del formulario de login
     * 2. Llama a loadUserByUsername() en CustomUserDetailsService
     * 3. Compara password ingresado con el hash almacenado usando BCrypt
     * 4. Si coincide, crea Authentication object con roles
     * 
     * @return DaoAuthenticationProvider configurado
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * BEAN: authenticationManager
     * DESCRIPCIÓN: Gestor de autenticación principal de Spring Security.
     * Necesario para autenticación programática (ej: cambio de contraseña).
     * 
     * @param config Configuración de autenticación
     * @return AuthenticationManager
     * @throws Exception Si hay error de configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * BEAN: securityFilterChain
     * DESCRIPCIÓN: Define la cadena de filtros de seguridad y políticas de acceso.
     * Este es el corazón de la configuración de seguridad de Spring.
     * 
     * POLÍTICAS DE SEGURIDAD CONFIGURADAS:
     * 
     * 1. URLs PÚBLICAS (permitAll):
     *    - /registro: Página de registro de nuevos profesores
     *    - /css/**, /js/**: Recursos estáticos
     *    
     * 2. URLs REQUIEREN AUTENTICACIÓN (authenticated):
     *    - Todas las demás URLs requieren login
     *    
     * 3. FORMULARIO DE LOGIN:
     *    - loginPage("/login"): Página custom de login
     *    - loginProcessingUrl("/login"): URL que procesa el POST
     *    - defaultSuccessUrl("/inicio"): Redirección tras login exitoso
     *    - failureUrl("/login?error=true"): Redirección tras fallo
     *    - permitAll(): Cualquiera puede acceder al login
     * 
     * 4. LOGOUT:
     *    - logoutUrl("/logout"): URL para cerrar sesión
     *    - invalidateHttpSession(true): Invalida la sesión HTTP
     *    - deleteCookies("JSESSIONID"): Elimina cookie de sesión
     *    - logoutSuccessUrl("/login?logout=true"): Redirección post-logout
     * 
     * 5. CSRF PROTECTION:
     *    - enable(): Habilita protección Cross-Site Request Forgery
     *    - Requiere token _csrf en todos los formularios POST/PUT/DELETE
     * 
     * 6. HEADERS DE SEGURIDAD:
     *    - frameOptions().deny(): Previene clickjacking
     *    - cacheControl().disable(): Previene caching de páginas sensibles
     * 
     * @param http HttpSecurity builder
     * @return SecurityFilterChain configurado
     * @throws Exception Si hay error de configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configurar proveedor de autenticación personalizado
            .authenticationProvider(authenticationProvider())
            
            // Configurar autorización de URLs
            .authorizeHttpRequests(authz -> authz
                // URLs públicas (no requieren autenticación)
                .requestMatchers("/", "/login", "/error", "/registro", "/verificar/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Todas las demás URLs requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Configurar formulario de login personalizado
            .formLogin(form -> form
                // Página custom de login (Thymeleaf)
                .loginPage("/login")
                // URL que procesa el POST del login
                .loginProcessingUrl("/login")
                // Parámetros del formulario
                .usernameParameter("email")
                .passwordParameter("password")
                // Redirección tras login exitoso
                .defaultSuccessUrl("/inicio", true)
                // Redirección tras fallo de autenticación
                .failureUrl("/login?error=true")
                // Permitir acceso anónimo al login
                .permitAll()
            )
            
            // Configurar logout
            .logout(logout -> logout
                // URL para cerrar sesión
                .logoutUrl("/logout")
                // Invalidar sesión HTTP
                .invalidateHttpSession(true)
                // Eliminar cookies de sesión
                .deleteCookies("JSESSIONID")
                // Redirección tras logout exitoso
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            
            // Habilitar protección CSRF (Cross-Site Request Forgery)
            .csrf(Customizer.withDefaults())
            
            // Configurar headers de seguridad
            .headers(headers -> headers
                // Prevenir clickjacking (X-Frame-Options)
                .frameOptions(frame -> frame.deny())
                // Deshabilitar cache para páginas sensibles
                .cacheControl(cache -> cache.disable())
            );
        
        return http.build();
    }
}
