package com.colegio.gestion.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * CONFIGURACIÓN JPA: JpaConfig
 * 
 * DESCRIPCIÓN:
 * Habilita la auditoría automática de entidades Spring Data JPA.
 * Los campos anotados con @CreatedDate, @LastModifiedDate, @CreatedBy,
 * @LastModifiedBy se llenan automáticamente al persistir/modificar entidades.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @EnableJpaAuditing: Activa el mecanismo de auditoría de JPA.
 *   Requiere un bean AuditorAware<T> para determinar el usuario actual.
 * 
 * AUDITORÍA DE ENTIDADES:
 * Las entidades que heredan de Persona obtienen automáticamente:
 * - fechaCreacion: Timestamp de creación (@CreatedDate)
 * - fechaModificacion: Timestamp de última modificación (@LastModifiedDate)
 * - creadoPor: Usuario que creó el registro (@CreatedBy)
 * - modificadoPor: Usuario que modificó (@LastModifiedBy)
 * 
 * FUNCIONAMIENTO:
 * 1. Al guardar una entidad, JPA llama a getCurrentAuditor()
 * 2. Obtiene el usuario del SecurityContext de Spring Security
 * 3. Llena los campos de auditoría automáticamente
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    /**
     * BEAN: auditorProvider
     * DESCRIPCIÓN: Proveedor de auditoría que obtiene el usuario actual.
     * Implementa AuditorAware<String> donde String es el tipo del auditor
     * (en este caso, el username/email del usuario autenticado).
     * 
     * FUNCIONAMIENTO:
     * 1. SecurityContextHolder.getContext() obtiene el contexto de seguridad
     * 2. getAuthentication() retorna la autenticación actual
     * 3. Si está autenticado, retorna el username (email)
     * 4. Si no, retorna Optional.empty() (sin auditor)
     * 
     * @return AuditorAware<String> que provee el usuario actual
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Obtener contexto de seguridad de Spring Security
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            // Verificar si hay usuario autenticado
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }
            
            // Retornar el username (email) del usuario autenticado
            // Esto llenará los campos @CreatedBy y @LastModifiedBy
            return Optional.ofNullable(authentication.getName());
        };
    }
}
