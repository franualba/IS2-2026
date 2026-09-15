package ar.edu.club.dto;

import ar.edu.club.domain.MedioPago;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** DTO de entrada: evita exponer entidades JPA directamente desde el formulario MVC. */
public record PagoCuotaRequest(
        @NotBlank String grupoFamiliarId,
        @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}") String periodo,
        @NotNull @Positive BigDecimal importe,
        @NotNull MedioPago medioPago) { }
