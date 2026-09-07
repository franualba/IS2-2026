package com.colegio.repositorio;

import com.colegio.modelo.Grado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradoRepository extends JpaRepository<Grado, Long> {
}