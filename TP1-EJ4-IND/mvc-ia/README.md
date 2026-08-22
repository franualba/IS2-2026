# Sistema de Gestion (Usuarios, Compras e Inventario)

Proyecto desarrollado en base al diagrama UML provisto, implementando:

- Registro de personas con datos personales (Nombre, Apellido, Documento, Fecha
  de Nacimiento, Correo Personal).
- Login usando el **correo personal como usuario**.
- Si el correo no esta registrado, se invita a la persona a registrarse.
- Si el usuario existe y **equivoca la clave 3 veces, se bloquea** automaticamente
  (queda en estado `BLOQUEADO`) y solo un Administrador puede desbloquearlo.
- Gestion de Productos, Inventario (movimientos de stock) y Compras (con
  descuento automatico de stock al comprar, y reposicion al anular).

## Stack tecnologico

| Capa                 | Tecnologia                                   |
|----------------------|-----------------------------------------------|
| Lenguaje / Framework | Java 17 + Spring Boot 3.2                     |
| Arquitectura         | MVC (Modelo - Vista - Controlador)            |
| Vista                | Thymeleaf + Bootstrap 5 (via WebJars)         |
| Persistencia / ORM   | Spring Data JPA (Hibernate) sobre MySQL       |
| Build                | Maven                                         |

## Estructura del proyecto

```
gestion-sistema/
├── pom.xml
└── src/main/
    ├── java/com/sistema/gestion/
    │   ├── GestionSistemaApplication.java   -> clase principal (main)
    │   ├── model/            -> ENTIDADES JPA (Modelo / capa M de MVC)
    │   │   ├── Persona.java          (superclase abstracta)
    │   │   ├── Usuario.java          (extends Persona)
    │   │   ├── Administrador.java    (extends Persona)
    │   │   ├── EstadoUsuario.java    (enum ACTIVO/BLOQUEADO)
    │   │   ├── Producto.java
    │   │   ├── Inventario.java
    │   │   ├── Compra.java
    │   │   └── DetalleCompra.java
    │   ├── repository/       -> INTERFACES JPA (acceso a datos / ORM)
    │   ├── service/          -> LOGICA DE NEGOCIO (interfaces + impl/)
    │   ├── controller/       -> CONTROLADORES (capa C de MVC)
    │   ├── dto/               -> objetos de formulario (LoginForm, RegistroUsuarioForm)
    │   ├── exception/        -> excepciones de negocio propias
    │   └── config/            -> configuracion (PasswordEncoder, interceptor de sesion)
    └── resources/
        ├── application.properties
        ├── templates/         -> VISTAS Thymeleaf (capa V de MVC)
        │   ├── fragments/      (head.html, navbar.html)
        │   ├── login.html, registro.html, home.html, admin-home.html, error.html
        │   ├── usuarios/list.html
        │   ├── productos/list.html, form.html
        │   ├── inventario/list.html, form.html
        │   └── compras/list.html, form.html
        └── static/css/styles.css
```

## Ajustes realizados sobre el diagrama UML (documentados)

Segun lo solicitado, se respeto el modelado original y solo se realizaron
2 ajustes puntuales, ambos documentados tambien como comentario Javadoc en
el codigo fuente correspondiente:

1. **Usuario**: el UML definia simultaneamente `bloqueado: Boolean` y el
   `ENUM estadoUsuario {ACTIVO, BLOQUEADO}` asociado a Usuario. Ambos
   representaban el mismo concepto de forma redundante (riesgo de
   inconsistencia, por ejemplo `bloqueado=false` con `estado=BLOQUEADO`).
   Se unifico usando unicamente el atributo `estado: EstadoUsuario`.
2. **DetalleCompra**: el UML solo definia el atributo `id`, lo cual es
   insuficiente para que el metodo `disminuirInventario()` pueda saber que
   producto y que cantidad descontar. Se agregaron los atributos
   `cantidad`, `precioUnitario` y la referencia a `Producto`.

## Como ejecutar el proyecto

### 1) Requisitos previos
- JDK 17 o superior
- Maven 3.9+ (o usar el wrapper si se agrega `mvnw`)
- MySQL Server 8.x en ejecucion

### 2) Configurar la base de datos
No es necesario crear la base de datos manualmente: la propiedad
`createDatabaseIfNotExist=true` (en `application.properties`) hace que MySQL
la cree sola la primera vez. Solo asegurate de que el usuario/clave de MySQL
configurados tengan permiso para crear bases de datos.

Editar `src/main/resources/application.properties` con tus credenciales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_sistema?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=TU_CLAVE
```

Hibernate creara automaticamente todas las tablas (`usuarios`,
`administradores`, `productos`, `inventario`, `compras`, `detalle_compra`)
gracias a `spring.jpa.hibernate.ddl-auto=update`.

### 3) Ejecutar

Desde la raiz del proyecto (donde esta el `pom.xml`):

```bash
mvn spring-boot:run
```

O bien, importar el proyecto como **"Maven Project"** en tu IDE preferido
(IntelliJ IDEA, Eclipse / STS, VS Code con extension Java) y ejecutar la
clase `GestionSistemaApplication`.

La aplicacion queda disponible en: **http://localhost:8080**

### 4) Primer uso

1. Ingresar a `http://localhost:8080/login`.
2. Como todavia no hay usuarios, escribir cualquier correo y hacer submit:
   el sistema detecta que no esta registrado y ofrece el enlace de
   **registro**.
3. Completar el formulario de registro (Nombre, Apellido, Documento, Fecha
   de Nacimiento, Correo, Contrasena).
4. Volver a `/login` e iniciar sesion con esas credenciales.
5. Probar el bloqueo: cerrar sesion y volver a intentar con una clave
   incorrecta 3 veces seguidas -> el usuario queda `BLOQUEADO`.

### 5) Cuenta de Administrador

El diagrama UML no define un flujo de **auto-registro** para
Administrador (a diferencia de Usuario), ya que se trata de una cuenta
privilegiada. Para crear el primer Administrador, la forma mas simple es
insertarlo directamente en la base de datos con una clave ya encriptada en
BCrypt (por ejemplo generada con https://bcrypt-generator.com/, o
programaticamente con `new BCryptPasswordEncoder().encode("miClave")`),
o exponer temporalmente un endpoint de alta protegido, segun las
necesidades de cada entorno. Una vez creado, el Administrador puede
iniciar sesion desde el mismo formulario `/login` (el sistema detecta
automaticamente si el correo pertenece a un Usuario o a un Administrador)
y desde su panel puede **desbloquear usuarios** bloqueados.

## Notas de arquitectura

- **MVC real**: los `Controller` son "delgados" (reciben la request y arman
  el `Model` para la vista); toda la logica de negocio (encriptar
  contrasenas, contar intentos fallidos, bloquear, descontar stock, etc.)
  vive en la capa `service`, que a su vez usa `repository` (ORM/JPA) para
  persistir. Las clases de `model` (entidades) contienen ademas los
  metodos de dominio definidos en el UML (`iniciarSesion`, `bloquear`,
  `resetearIntentos`, `agregarDetalle`, `disminuirInventario`, etc.).
- **Seguridad de contrasenas**: las claves nunca se guardan en texto plano;
  se usa `BCryptPasswordEncoder` (Spring Security Crypto).
- **Proteccion de rutas**: un `HandlerInterceptor` (`AuthInterceptor`)
  valida que exista sesion HTTP activa para acceder a `/home`, `/admin/**`,
  `/productos/**`, `/inventario/**` y `/compras/**`.
- Todas las clases incluyen comentarios detallados indicando a que capa
  pertenecen, que anotaciones se usan y por que, y su relacion con el
  diagrama UML original.
