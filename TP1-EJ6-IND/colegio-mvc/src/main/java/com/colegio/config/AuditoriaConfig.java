package com.colegio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * CONFIGURACIÓN DE AUDITORÍA JPA.
 *
 * @EnableJpaAuditing: activa el mecanismo de auditoría de Spring Data JPA
 *   (hace funcionar @CreatedDate, @CreatedBy, etc. en AuditoriaEntity).
 *
 * El Bean AuditorAware<String> indica a Spring QUÉ valor usar para "creadoPor /
 * modificadoPor": tomamos el nombre del usuario autenticado desde el
 * SecurityContext (el correo del docente). Si no hay sesión (ej. arranque,
 * datos iniciales) se devuelve Optional.empty() y el campo queda null.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditoriaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .filter(authentication -> authentication.isAuthenticated())
                .filter(a -> !"anonymousUser".equals(a.getName()))
            .map(authentication -> authentication.getName());
    }
}