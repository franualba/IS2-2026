package com.colegio.repositorio;

import com.colegio.modelo.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    List<Alumno> findByEliminadoFalse();
}