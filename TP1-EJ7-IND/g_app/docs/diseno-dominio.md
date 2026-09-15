# Rediseño del dominio

El pago pertenece a `GrupoFamiliar`, no a `Socio`: la cuota es familiar y debe existir como máximo una vez para cada período. `MedioPago` es un enum extensible que actualmente contempla efectivo, transferencia y Mercado Pago.

```mermaid
classDiagram
    class AuditableEntity { creadoEn; actualizadoEn; creadoPor; actualizadoPor }
    class Persona { id; nombre; apellido; fechaNacimiento; eliminado }
    class Socio { fechaAlta; estado }
    class Imagen { nombre; mime; contenido; eliminado }
    class GrupoFamiliar { denominacion }
    class PagoCuota { periodo; importe; medioPago; fechaPago }
    class RegistroAcceso { fecha; horaEntrada; horaSalida; obtenerDuracion() }
    class Actividad { nombre; horario; cupos; eliminado }
    class MedioPago { EFECTIVO; TRANSFERENCIA; MERCADO_PAGO }
    AuditableEntity <|-- Persona
    AuditableEntity <|-- Imagen
    AuditableEntity <|-- GrupoFamiliar
    AuditableEntity <|-- PagoCuota
    AuditableEntity <|-- RegistroAcceso
    AuditableEntity <|-- Actividad
    Persona <|-- Socio
    Persona "1" --> "0..1" Imagen
    GrupoFamiliar "1" o-- "0..*" Socio
    GrupoFamiliar "1" *-- "0..*" PagoCuota
    PagoCuota --> MedioPago
    Persona "1" --> "0..*" RegistroAcceso
    Socio "1" o-- "0..*" Actividad
```

## Capas

`ClubController` sólo traduce HTTP a DTO y vista. `PagoCuotaService` aplica la regla de negocio dentro de una transacción. Los repositorios abstraen JPA/PostgreSQL. Spring Security protege los casos de uso y Spring Data Auditing registra usuario y timestamps.
