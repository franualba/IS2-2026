package com.colegio.gestion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.colegio.gestion.entity.Aula;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {
    List<Aula> findAll();
}
