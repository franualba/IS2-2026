# Gestión de productos

Aplicación de gestión de productos con operaciones para agregar, listar, consultar,
editar y eliminar registros. Incluye una interfaz React y una API REST con Spring
Boot.

Cada producto tiene un identificador, un nombre y un precio. El backend guarda los
datos en una base H2 local basada en archivos para conservarlos entre reinicios.

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- Node.js 18 o superior y npm

## Ejecutar en desarrollo

Abrí dos terminales desde la carpeta del proyecto.

Terminal 1 — API:

```powershell
cd backend
mvn spring-boot:run
```

Terminal 2 — interfaz:

```powershell
cd frontend
npm install
npm run dev
```

Abrí la URL que muestra Vite (por defecto `http://localhost:5173`). La interfaz
envía las solicitudes `/api` al backend en `http://localhost:8080`.

## API REST

| Método | Ruta | Acción |
| --- | --- | --- |
| `GET` | `/api/productos` | Listar productos |
| `GET` | `/api/productos/{id}` | Consultar un producto |
| `POST` | `/api/productos` | Crear un producto |
| `PUT` | `/api/productos/{id}` | Actualizar un producto |
| `DELETE` | `/api/productos/{id}` | Eliminar un producto |

El cuerpo para crear o actualizar debe tener esta forma:

```json
{
  "nombre": "Teclado mecánico",
  "precio": 12500.50
}
```

El precio debe ser mayor a cero y admite hasta dos decimales.

## Pruebas

Desde `backend`, ejecutá `mvn test`. Para verificar la compilación de la interfaz,
desde `frontend`, ejecutá `npm run build`.
