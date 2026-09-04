package com.is2.tp1ej6ind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación.
 *
 * Arquitectura aplicada:
 * - MVC: controladores, servicios, repositorios y vistas.
 * - Spring Boot: inicialización del contexto y configuración automática.
 * - Thymeleaf: renderización de vistas en el lado del cliente.
 * - JPA/Hibernate: persistencia ORM con PostgreSQL.
 * - Spring Security: autenticación/autorización por email y contraseña.
 * - Auditing: trazabilidad de creación/actualización de entidades.
 *
 * La aplicación modela un sistema escolar donde los docentes se registran,
 * autentican y gestionan la relación con grados, materias, aulas y alumnos.
 */
@SpringBootApplication
public class Tp1Ej6IndApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp1Ej6IndApplication.class, args);
    }
}
