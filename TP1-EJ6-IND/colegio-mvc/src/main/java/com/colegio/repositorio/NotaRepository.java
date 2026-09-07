package com.colegio.repositorio;

import com.colegio.modelo.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    /** Implementa listarNotas() del diagrama para un alumno dado. */
    List<Nota> findByAlumnoIdAlumno(Long idAlumno);
}