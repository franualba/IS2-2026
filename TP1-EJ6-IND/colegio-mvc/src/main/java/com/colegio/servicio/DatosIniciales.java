package com.colegio.servicio;

import com.colegio.modelo.*;
import com.colegio.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DATOS DE ARRANQUE (solo si la BD está vacía):
 *  - Roles ROLE_PROFESOR y ROLE_ADMIN.
 *  - Usuario administrador: admin@colegio.com / Admin123!  (CAMBIAR en producción)
 *  - Un colegio, un grado y un aula de ejemplo para poblar los <select>.
 *
 * CommandLineRunner: se ejecuta una vez al iniciar la aplicación.
 */
@Component
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final ProfesorRepository profesorRepository;
    private final ColegioRepository colegioRepository;
    private final GradoRepository gradoRepository;
    private final AulaRepository aulaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (rolRepository.count() > 0) return; // ya inicializado

        Rol profesor = new Rol(); profesor.setNombre("ROLE_PROFESOR");
        Rol admin = new Rol();    admin.setNombre("ROLE_ADMIN");
        rolRepository.save(profesor);
        rolRepository.save(admin);

        // Usuario administrador de ejemplo (hash BCrypt).
        Profesor administrador = new Profesor();
        administrador.setNombre("Admin");
        administrador.setApellido("Sistema");
        administrador.setSexo(Sexo.OTRO);
        administrador.setFechaNacimiento(java.time.LocalDate.of(1990, 1, 1));
        administrador.setUsuario("admin@colegio.com");
        administrador.setPassword(passwordEncoder.encode("Admin123!"));
        administrador.getRoles().add(admin);
        administrador.getRoles().add(profesor);
        profesorRepository.save(administrador);

        // Catálogo mínimo de ejemplo.
        Colegio colegio = new Colegio();
        colegio.setNombre("Colegio Nacional");
        colegio.setDireccion("Av. Siempre Viva 123");
        colegioRepository.save(colegio);

        Grado grado = new Grado();
        grado.setNivel("1er Año");
        grado.setColegio(colegio);
        gradoRepository.save(grado);

        Aula aula = new Aula();
        aula.setDivision("A");
        grado.agregarAula(aula);   // método de negocio del diagrama
        aulaRepository.save(aula);
    }
}