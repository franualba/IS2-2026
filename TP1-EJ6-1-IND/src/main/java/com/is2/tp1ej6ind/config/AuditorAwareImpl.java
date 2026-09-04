package com.is2.tp1ej6ind.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementación del auditor de JPA.
 *
 * Permite registrar quién creó o modificó una entidad en tiempo de ejecución.
 * Si el usuario no está autenticado, se devuelve un valor vacío.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            return Optional.of("system");
        }

        return Optional.of(authentication.getName());
    }
}
