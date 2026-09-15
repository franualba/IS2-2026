package ar.edu.club.service;

import ar.edu.club.domain.*;
import ar.edu.club.dto.PagoCuotaRequest;
import ar.edu.club.repository.*;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagoCuotaServiceTest {
    private final GrupoFamiliarRepository grupos = mock(GrupoFamiliarRepository.class);
    private final PagoCuotaRepository pagos = mock(PagoCuotaRepository.class);
    private final PagoCuotaService service = new PagoCuotaService(grupos, pagos);

    @Test void registraCuotaConMedioSeleccionado() {
        GrupoFamiliar grupo = new GrupoFamiliar("Familia Perez");
        PagoCuotaRequest request = new PagoCuotaRequest("id", "2026-09", new BigDecimal("15000"), MedioPago.TRANSFERENCIA);
        when(grupos.findById("id")).thenReturn(Optional.of(grupo));
        when(pagos.existsByGrupoFamiliarIdAndPeriodo("id", "2026-09")).thenReturn(false);
        service.registrar(request);
        verify(pagos).save(any(PagoCuota.class));
    }

    @Test void rechazaCuotaDuplicada() {
        when(grupos.findById("id")).thenReturn(Optional.of(new GrupoFamiliar("Familia")));
        when(pagos.existsByGrupoFamiliarIdAndPeriodo("id", "2026-09")).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> service.registrar(new PagoCuotaRequest("id", "2026-09", BigDecimal.TEN, MedioPago.EFECTIVO)));
        verify(pagos, never()).save(any());
    }
}
