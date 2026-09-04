package com.colegio.gestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * CLASE PRINCIPAL: ColegioManagementSystemApplication
 * 
 * DESCRIPCIÓN:
 * Clase principal de la aplicación Spring Boot.
 * Contiene el método main que inicia el servidor embebido Tomcat.
 * 
 * ANOTACIONES IMPORTANTES:
 * - @SpringBootApplication: Anotación compuesta que incluye:
 *   - @Configuration: Marca como clase de configuración
 *   - @EnableAutoConfiguration: Habilita auto-configuración de Spring Boot
 *   - @ComponentScan: Escanea componentes en paquetes hijos
 * 
 * ARQUITECTURA DEL PROYECTO:
 * 
 * com.colegio.gestion
 * ├── entity/           # Entidades JPA (modelo de datos)
 * │   ├── Persona       # Superclase abstracta con auditoría
 * │   ├── Profesor      # Hereda de Persona, usuario del sistema
 * │   ├── Alumno        # Hereda de Persona
 * │   ├── Colegio       # Institución educativa
 * │   ├── Grado         # Nivel educativo
 * │   ├── Aula          # División/sección
 * │   ├── Materia       # Asignatura
 * │   ├── DictadoClases # Instancia de dictado
 * │   └── Nota          # Calificación
 * │
 * ├── repository/       # Capa de persistencia (JPA Repositories)
 * │   ├── ProfesorRepository
 * │   ├── AlumnoRepository
 * │   ├── ColegioRepository
 * │   └── ...
 * │
 * ├── service/          # Capa de lógica de negocio
 * │   ├── CustomUserDetailsService  # Autenticación Spring Security
 * │   └── ...
 * │
 * ├── controller/       # Capa de presentación (MVC Controllers)
 * │   └── ...
 * │
 * ├── config/           # Configuración de seguridad y JPA
 * │   ├── SecurityConfig    # Spring Security
 * │   └── JpaConfig         # Auditoría JPA
 * │
 * ├── dto/              # Data Transfer Objects
 * └── mapper/           # Convertidores Entity <-> DTO
 * 
 * CAPAS DE LA APLICACIÓN (Arquitectura MVC):
 * 
 * 1. PRESENTACIÓN (Controller + Thymeleaf):
 *    - Maneja requests HTTP
 *    - Renderiza vistas HTML
 *    - Valida formularios
 * 
 * 2. NEGOCIO (Service):
 *    - Lógica de negocio
 *    - Transacciones
 *    - Validaciones complejas
 * 
 * 3. PERSISTENCIA (Repository):
 *    - Operaciones CRUD
 *    - Consultas a BD
 *    - Mapeo ORM
 * 
 * SEGURIDAD IMPLEMENTADA:
 * - Spring Security con autenticación por formulario
 * - BCrypt para encriptación de contraseñas
 * - Roles de usuario (ROLE_DOCENTE)
 * - Protección CSRF
 * - Sesiones seguras
 * 
 * @author Sistema Colegio
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.colegio.gestion")
public class ColegioManagementSystemApplication {

    /**
     * MÉTODO MAIN: Punto de entrada de la aplicación
     * 
     * DESCRIPCIÓN:
     * Inicia la aplicación Spring Boot que:
     * 1. Crea el ApplicationContext de Spring
     * 2. Configura todos los beans (@Service, @Repository, etc.)
     * 3. Inicia el servidor web embebido (Tomcat en puerto 8080)
     * 4. Conecta a la base de datos PostgreSQL
     * 5. Aplica migraciones de esquema (ddl-auto=update)
     * 
     * FLUJO DE INICIO:
     * - Lee application.properties para configuración
     * - Escanea paquetes buscando componentes Spring
     * - Configura Spring Security según SecurityConfig
     * - Inicializa pool de conexiones a PostgreSQL
     * - Despliega aplicación en contexto raíz (/)
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // SpringApplication.run() inicia el contexto de aplicación
        // Retorna ApplicationContext configurado
        SpringApplication.run(ColegioManagementSystemApplication.class, args);
        
        // Mensaje de éxito en consola
        System.out.println("=================================================");
        System.out.println("  COLEGIO MANAGEMENT SYSTEM INICIADO");
        System.out.println("  Servidor corriendo en: http://localhost:8080");
        System.out.println("  Login: /login");
        System.out.println("=================================================");
    }
}
