package ar.edu.club.repository;

import ar.edu.club.domain.PagoCuota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoCuotaRepository extends JpaRepository<PagoCuota, String> {
    boolean existsByGrupoFamiliarIdAndPeriodo(String grupoFamiliarId, String periodo);
}
