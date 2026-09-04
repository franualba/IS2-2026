package com.is2.tp1ej6ind.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración del auditado de entidades JPA.
 *
 * Con esta anotación, las fechas de creación y actualización quedan
 * gestionadas automáticamente por Spring Data JPA.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
