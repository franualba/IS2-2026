package com.colegio.servicio;

import com.colegio.dto.MateriaDTO;
import com.colegio.modelo.Materia;
import com.colegio.repositorio.ColegioRepository;
import com.colegio.repositorio.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/** SERVICIO DE MATERIAS: CRUD con DTOs y eliminación lógica. */
@Service
@RequiredArgsConstructor
public class MateriaServicio {

    private final MateriaRepository materiaRepository;
    private final ColegioRepository colegioRepository;

    @Transactional(readOnly = true)
    public List<MateriaDTO> listar() {
        return materiaRepository.findByEliminadoFalse().stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public MateriaDTO buscar(Long id) {
        return aDTO(materiaRepository.findById(Objects.requireNonNull(id, "id no puede ser null"))
                .orElseThrow(() -> new IllegalStateException("Materia no encontrada")));
    }

    @Transactional
    public void guardar(MateriaDTO dto) {
        Objects.requireNonNull(dto, "dto no puede ser null");
        Long idMateria = dto.getIdMateria();
        Materia m = idMateria == null ? new Materia()
            : materiaRepository.findById(idMateria)
                    .orElseThrow(() -> new IllegalStateException("Materia no encontrada"));
        m.setNombre(dto.getNombre());
        Long colegioId = dto.getColegioId();
        if (colegioId != null) {
            colegioRepository.findById(colegioId).ifPresent(m::setColegio);
        }
        materiaRepository.save(m);
    }

    @Transactional
    public void eliminarLogico(Long id) {
        materiaRepository.findById(Objects.requireNonNull(id, "id no puede ser null")).ifPresent(m -> {
            m.setEliminado(true);
            materiaRepository.save(m);
        });
    }

    public long contar() { return materiaRepository.findByEliminadoFalse().size(); }

    private MateriaDTO aDTO(Materia m) {
        MateriaDTO dto = new MateriaDTO();
        dto.setIdMateria(m.getIdMateria());
        dto.setNombre(m.getNombre());
        dto.setColegioId(m.getColegio() != null ? m.getColegio().getIdColegio() : null);
        dto.setColegioNombre(m.getColegio() != null ? m.getColegio().getNombre() : "—");
        return dto;
    }
}