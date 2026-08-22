package com.sistema.gestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================================
 * CLASE PRINCIPAL DE LA APLICACION
 * ============================================================================
 * Punto de entrada de la aplicacion Spring Boot.
 *
 * La anotacion @SpringBootApplication es una anotacion "combo" que agrupa:
 *   - @Configuration        -> permite definir beans dentro de esta clase
 *   - @EnableAutoConfiguration -> Spring Boot configura automaticamente
 *                                 el servidor web, JPA, Thymeleaf, etc. en
 *                                 base a las dependencias del pom.xml
 *   - @ComponentScan        -> escanea el paquete com.sistema.gestion y
 *                                 sub-paquetes en busca de @Component,
 *                                 @Service, @Repository, @Controller, etc.
 *
 * Al ejecutar este main() se levanta el servidor embebido (Tomcat) y queda
 * disponible el sistema en http://localhost:8080
 * ============================================================================
 */
@SpringBootApplication
public class GestionSistemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionSistemaApplication.class, args);
    }
}
