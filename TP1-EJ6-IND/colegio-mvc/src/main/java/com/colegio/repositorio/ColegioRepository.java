package com.colegio.repositorio;

import com.colegio.modelo.Colegio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColegioRepository extends JpaRepository<Colegio, Long> {
    List<Colegio> findByEliminadoFalse();
}