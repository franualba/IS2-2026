package ar.edu.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Punto de entrada. @EnableJpaAuditing activa createdAt/updatedAt y usuario auditor. */
@SpringBootApplication
@EnableJpaAuditing
public class ClubDeportivoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClubDeportivoApplication.class, args);
    }
}
