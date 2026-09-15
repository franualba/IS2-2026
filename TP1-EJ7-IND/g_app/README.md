# Club Deportivo

Aplicación Spring Boot para administrar personas, socios, grupos familiares, accesos, actividades, imágenes y pagos de cuota.

## Arquitectura

- `domain`: entidades JPA, herencia `Persona` -> `Socio`, agregado `GrupoFamiliar` y auditoría.
- `repository`: persistencia Spring Data JPA sobre PostgreSQL.
- `service`: casos de uso transaccionales y reglas de negocio.
- `dto`: contratos entre web y aplicación; no se exponen entidades desde formularios.
- `web`: controladores MVC y vistas Thymeleaf.
- `config`: Spring Security y `AuditorAware`.

## Ejecutar

1. Levantar PostgreSQL: `docker compose up -d db`.
2. Ejecutar con Java 17 y Maven: `mvn spring-boot:run`.
3. Abrir `http://localhost:8080/login`.
4. Usuario demo: `admin` / `admin` (cambiar en producción).

## Testing

- Unitario: `mvn test` valida reglas de `PagoCuotaService` con Mockito.
- Carga: `k6 run tests/load/club-load.js` simula usuarios concurrentes consultando familias.
- Stress: `k6 run --vus 100 --duration 5m tests/load/club-load.js`; observar p95, tasa de errores y saturación de PostgreSQL.
- La prueba de integración PostgreSQL puede ejecutarse con Testcontainers cuando Docker esté disponible.

## Diseño de pago

Cada familia registra una cuota por período `YYYY-MM`, importe y `EFECTIVO`, `TRANSFERENCIA` o `MERCADO_PAGO`. La regla se valida en servicio y se refuerza con una restricción única en base.
