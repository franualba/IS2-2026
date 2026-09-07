package com.colegio.servicio;

import com.colegio.dto.AlumnoDTO;
import com.colegio.modelo.Alumno;
import com.colegio.repositorio.AlumnoRepository;
import com.colegio.repositorio.AulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * SERVICIO DE ALUMNOS: CRUD con mapeo DTO↔Entidad y eliminación lógica.
 * El controlador NUNCA toca la entidad: recibe y entrega solo DTOs.
 */
@Service
@RequiredArgsConstructor
public class AlumnoServicio {

    private final AlumnoRepository alumnoRepository;
    private final AulaRepository aulaRepository;

    @Transactional(readOnly = true)
    public List<AlumnoDTO> listar() {
        return alumnoRepository.findByEliminadoFalse().stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public AlumnoDTO buscar(Long id) {
        Alumno a = alumnoRepository.findById(Objects.requireNonNull(id, "id no puede ser null"))
                .orElseThrow(() -> new IllegalStateException("Alumno no encontrado"));
        return aDTO(a);
    }

    /** registrarAlumno / editarAlumno del diagrama. */
    @Transactional
    public void guardar(AlumnoDTO dto) {
        Objects.requireNonNull(dto, "dto no puede ser null");
        Long idAlumno = dto.getIdAlumno();
        Alumno alumno = idAlumno == null ? new Alumno()
            : alumnoRepository.findById(idAlumno)
                    .orElseThrow(() -> new IllegalStateException("Alumno no encontrado"));

        alumno.setNombre(dto.getNombre());
        alumno.setApellido(dto.getApellido());
        alumno.setSexo(dto.getSexo());
        alumno.setFechaNacimiento(dto.getFechaNacimiento());
        Long aulaId = dto.getAulaId();
        if (aulaId != null) {
            aulaRepository.findById(aulaId).ifPresent(alumno::setAula);
        }
        alumnoRepository.save(alumno);
    }

    /** eliminarAlumno del diagrama (lógico). */
    @Transactional
    public void eliminarLogico(Long id) {
        alumnoRepository.findById(Objects.requireNonNull(id, "id no puede ser null")).ifPresent(a -> {
            a.setEliminado(true);
            alumnoRepository.save(a);
        });
    }

    public long contar() { return alumnoRepository.findByEliminadoFalse().size(); }

    /** Mapeo Entidad → DTO (lectura). */
    private AlumnoDTO aDTO(Alumno a) {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setIdAlumno(a.getIdAlumno());
        dto.setNombre(a.getNombre());
        dto.setApellido(a.getApellido());
        dto.setSexo(a.getSexo());
        dto.setFechaNacimiento(a.getFechaNacimiento());
        dto.setAulaId(a.getAula() != null ? a.getAula().getIdAula() : null);
        dto.setAulaDescripcion(a.getAula() != null
                ? a.getAula().getGrado().getNivel() + " " + a.getAula().getDivision() : "Sin aula");
        return dto;
    }
}