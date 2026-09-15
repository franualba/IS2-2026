package ar.edu.club.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

/** Provee a JPA el usuario actual para @CreatedBy y @LastModifiedBy. */
@Configuration
public class AuditingConfig {
    @Bean
    AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> authentication.isAuthenticated())
                .map(authentication -> authentication.getName())
                .or(() -> Optional.of("sistema"));
    }
}
