package com.colegio.gestion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.colegio.gestion.entity.Nota;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {
    List<Nota> findAll();
}
