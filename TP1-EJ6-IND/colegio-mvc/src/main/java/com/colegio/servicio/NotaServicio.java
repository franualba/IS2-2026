package com.colegio.servicio;

import com.colegio.dto.NotaDTO;
import com.colegio.modelo.Nota;
import com.colegio.repositorio.AlumnoRepository;
import com.colegio.repositorio.MateriaRepository;
import com.colegio.repositorio.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * SERVICIO DE NOTAS: implementa listarNotas() del diagrama (por alumno y global)
 * trabajando exclusivamente con NotaDTO hacia el controlador.
 */
@Service
@RequiredArgsConstructor
public class NotaServicio {

    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;
    private final MateriaRepository materiaRepository;

    @Transactional(readOnly = true)
    public List<NotaDTO> listar() {
        return notaRepository.findAll().stream().map(this::aDTO).toList();
    }

    /** listarNotas de un alumno específico (método del diagrama en Alumno). */
    @Transactional(readOnly = true)
    public List<NotaDTO> listarNotasPorAlumno(Long idAlumno) {
        return notaRepository.findByAlumnoIdAlumno(idAlumno).stream().map(this::aDTO).toList();
    }

    @Transactional
    public void guardar(NotaDTO dto) {
        Objects.requireNonNull(dto, "dto no puede ser null");
        Long idNota = dto.getIdNota();
        Nota nota = idNota == null ? new Nota()
            : notaRepository.findById(idNota)
                    .orElseThrow(() -> new IllegalStateException("Nota no encontrada"));
        nota.setValor(dto.getValor());
        nota.setFecha(dto.getFecha());
        alumnoRepository.findById(Objects.requireNonNull(dto.getAlumnoId(), "alumnoId no puede ser null"))
            .ifPresent(nota::setAlumno);
        materiaRepository.findById(Objects.requireNonNull(dto.getMateriaId(), "materiaId no puede ser null"))
            .ifPresent(nota::setMateria);
        notaRepository.save(nota);
    }

    @Transactional
    public void eliminar(Long id) {
        notaRepository.deleteById(Objects.requireNonNull(id, "id no puede ser null"));
    }

    private NotaDTO aDTO(Nota n) {
        NotaDTO dto = new NotaDTO();
        dto.setIdNota(n.getIdNota());
        dto.setValor(n.getValor());
        dto.setFecha(n.getFecha());
        dto.setAlumnoId(n.getAlumno() != null ? n.getAlumno().getIdAlumno() : null);
        dto.setMateriaId(n.getMateria() != null ? n.getMateria().getIdMateria() : null);
        dto.setAlumnoNombre(n.getAlumno() != null
                ? n.getAlumno().getNombre() + " " + n.getAlumno().getApellido() : "—");
        dto.setMateriaNombre(n.getMateria() != null ? n.getMateria().getNombre() : "—");
        return dto;
    }
}