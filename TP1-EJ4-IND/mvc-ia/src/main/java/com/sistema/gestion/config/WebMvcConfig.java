package com.sistema.gestion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ============================================================================
 * CONFIGURACION - WebMvcConfig
 * ============================================================================
 * Registra el AuthInterceptor sobre las rutas privadas del sistema,
 * excluyendo explicitamente las rutas publicas (login, registro, recursos
 * estaticos de Bootstrap/CSS/JS, pagina de error).
 * ============================================================================
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/home/**", "/admin/**", "/productos/**",
                        "/inventario/**", "/compras/**")
                .excludePathPatterns("/login", "/registro", "/css/**", "/js/**",
                        "/webjars/**", "/error");
    }
}
