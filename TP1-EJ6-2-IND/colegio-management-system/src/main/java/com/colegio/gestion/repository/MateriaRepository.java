package com.colegio.gestion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.colegio.gestion.entity.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Integer> {
    List<Materia> findByEliminadoFalse();
    boolean existsByNombre(String nombre);
}
