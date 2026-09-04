# Sistema de Gestión Escolar (Spring Boot + MVC + Thymeleaf + PostgreSQL + Seguridad + Auditoría)

## Requisitos
- Java 17+, Maven, PostgreSQL corriendo en localhost:5432.

## Puesta en marcha
1. Crear la base de datos: `CREATE DATABASE colegio_db;`
2. Editar `src/main/resources/application.properties` (usuario/clave de PostgreSQL y credenciales SMTP).
3. Correo de bienvenida (Gmail): activar verificación en 2 pasos y generar una
   "Contraseña de aplicación" en https://myaccount.google.com/apppasswords;
   ponerla en `spring.mail.password`.
4. Ejecutar: `mvn spring-boot:run` → http://localhost:8080
5. Hibernate crea las tablas automáticamente (`ddl-auto=update`) y `DatosIniciales` siembra:
   - Roles ROLE_PROFESOR / ROLE_ADMIN
   - Admin: **admin@colegio.com / Admin123!** (cambiar en producción)
   - Colegio, grado y aula de ejemplo.
6. Registrar un docente en `/registro` (recibirá correo de bienvenida) y loguearse en `/login`.

## Credenciales de prueba
| Usuario | Contraseña | Rol |
|---|---|---|
| admin@colegio.com | Admin123! | ADMIN + PROFESOR |
| (correo registrado) | (elegida al registrar) | PROFESOR |