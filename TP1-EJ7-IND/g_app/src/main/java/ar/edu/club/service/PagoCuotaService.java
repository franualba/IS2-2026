package ar.edu.club.service;

import ar.edu.club.domain.*;
import ar.edu.club.dto.PagoCuotaRequest;
import ar.edu.club.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Capa de aplicación: coordina la validación de negocio y la persistencia transaccional.
 * La regla de no duplicar período se valida aquí y se refuerza en la base mediante UNIQUE.
 */
@Service
public class PagoCuotaService {
    private final GrupoFamiliarRepository grupos;
    private final PagoCuotaRepository pagos;
    public PagoCuotaService(GrupoFamiliarRepository grupos, PagoCuotaRepository pagos) { this.grupos = grupos; this.pagos = pagos; }

    @Transactional
    public void registrar(PagoCuotaRequest request) {
        GrupoFamiliar grupo = grupos.findById(request.grupoFamiliarId()).orElseThrow(() -> new IllegalArgumentException("Familia inexistente"));
        if (pagos.existsByGrupoFamiliarIdAndPeriodo(request.grupoFamiliarId(), request.periodo())) throw new IllegalStateException("La cuota del período ya fue registrada");
        pagos.save(new PagoCuota(grupo, request.periodo(), request.importe(), request.medioPago()));
    }
}
