package com.is2.tp1ej6ind.service;

import com.is2.tp1ej6ind.dto.ProfesorRegistroDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProfesorServiceTest {

    @Test
    void testCrearAlumnoDesdeDTO() {
        ProfesorRegistroDTO dto = new ProfesorRegistroDTO();
        dto.setNombre("Ana");
        dto.setApellido("Pérez");
        dto.setSexo("Femenino");
        dto.setFechaNacimiento(LocalDate.of(1988, 5, 15));
        dto.setEmail("ana@colegio.edu.ar");
        dto.setPassword("Password123");

        assertNotNull(dto);
        assertEquals("Ana", dto.getNombre());
        assertEquals("ana@colegio.edu.ar", dto.getEmail().toLowerCase());
    }
}
