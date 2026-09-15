package ar.edu.club.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RegistroAccesoTest {
    @Test void accesoSinSalidaTieneDuracionCero() {
        RegistroAcceso acceso = new RegistroAcceso(new Persona("Ana", "Lopez"));
        acceso.registrarEntrada();
        assertEquals(0, acceso.obtenerDuracion().toSeconds());
    }
}
