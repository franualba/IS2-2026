package com.colegio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CLASE PRINCIPAL (punto de entrada).
 *
 * @SpringBootApplication es una meta-anotación que combina:
 *  - @Configuration:     la clase puede declarar Beans.
 *  - @EnableAutoConfiguration: Spring Boot configura automáticamente DataSource, JPA,
 *                        Security, Thymeleaf, etc. según las dependencias del pom.
 *  - @ComponentScan:     escanea los paquetes com.colegio.** buscando
 *                        @Controller, @Service, @Repository, @Configuration.
 */
@SpringBootApplication
public class ColegioApplication {
    public static void main(String[] args) {
        SpringApplication.run(ColegioApplication.class, args);
    }
}