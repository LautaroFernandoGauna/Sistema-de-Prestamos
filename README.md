# Sistema de Préstamos de Equipos Tecnológicos

API REST para administrar el inventario y el préstamo de equipos
tecnológicos, gestionar solicitantes y controlar devoluciones,
vencimientos y multas.

## Características principales

-   Gestión de usuarios con autenticación mediante JWT.
-   Autorización basada en roles: `ADMIN`, `ENCARGADO` y `USER`.
-   Gestión de solicitantes y asignación de perfiles: `ALUMNO`,
    `DOCENTE` y `STAFF`.
-   Registro y devolución de préstamos.
-   Validación de disponibilidad de los equipos para evitar préstamos
    duplicados.
-   Cálculo automático de fechas de vencimiento y multas según el perfil
    del solicitante.
-   Generación de reportes de préstamos vencidos, activos e historial.
-   Exportación de reportes en formato CSV.
-   Respuestas de error HTTP estandarizadas para validar datos y
    comunicar conflictos de negocio.

## Tecnologías

  Tecnología             Uso
  ---------------------- ----------------------------------------
  Java 21                Lenguaje de programación
  Spring Boot            Framework principal de la aplicación
  Spring Web             Exposición de endpoints REST
  Spring Data JPA        Persistencia y acceso a datos
  Hibernate              Implementación ORM de JPA
  Spring Security        Autenticación y autorización
  JWT (JJWT)             Generación y validación de tokens
  BCrypt                 Hash de contraseñas
  H2 Database            Base de datos en memoria
  Maven                  Gestión de dependencias y construcción
  Postman / PowerShell   Pruebas de endpoints HTTP

## Arquitectura

La aplicación utiliza una arquitectura monolítica organizada en capas:

-   **Controller:** recibe las solicitudes HTTP, valida la entrada y
    devuelve las respuestas.
-   **Service:** contiene las reglas y operaciones de negocio.
-   **Repository:** realiza las operaciones de persistencia mediante
    JPA.
-   **Model:** contiene las entidades y enumeraciones del dominio.
-   **DTO:** define los datos que recibe y devuelve la API.
-   **Security:** configura Spring Security, el filtro JWT y las reglas
    de acceso.
-   **Strategy:** implementa las políticas de plazo y cálculo de multas
    por perfil.
-   **Template:** contiene la estructura común para generar reportes.

El flujo habitual de una solicitud es:

`Cliente HTTP → Controller → Service → Repository → Base de datos`

Las respuestas de la API se envían en JSON, excepto las descargas de
reportes CSV.

## Roles y permisos

  Rol           Responsabilidades
  ------------- ------------------------------------------------------
  `ADMIN`       Gestionar equipos y asignar perfiles a las personas.
  `ENCARGADO`   Registrar préstamos y devoluciones.
  `USER`        Consultar equipos y sus propios préstamos.

El registro público crea usuarios con rol `USER` y perfil inicial
`ALUMNO`. El cliente no puede asignarse el rol `ADMIN` mediante el
endpoint de registro.

## Perfiles, plazos y multas

Las políticas de préstamo dependen del perfil del solicitante:

  Perfil         Plazo Multa por demora
  ----------- -------- --------------------------------
  `ALUMNO`      3 días \$500 por cada día de atraso
  `DOCENTE`     7 días \$2.000 fijos si existe atraso
  `STAFF`       7 días Sin multa

El sistema selecciona automáticamente la política correspondiente al
perfil. Al devolver un equipo, calcula los días de retraso, determina la
multa y actualiza el estado del préstamo y del equipo.

## Autenticación

La API utiliza JWT y no mantiene sesiones de usuario en el servidor.

### Registro

`POST /api/auth/signup`

Registra un solicitante. La cuenta se crea con contraseña protegida
mediante BCrypt, rol `USER` y perfil `ALUMNO`.

### Inicio de sesión

`POST /api/auth/signin`

Autentica al usuario. Si las credenciales son correctas, devuelve un
token JWT junto con los datos del usuario y su rol.

### Envío del token

Para acceder a endpoints protegidos, incluir el token en el encabezado
HTTP:

``` http
Authorization: Bearer <token>
```

El filtro de autenticación valida el token en cada solicitud y comprueba
que no haya sido revocado mediante el mecanismo de cierre de sesión.

> Los endpoints de registro e inicio de sesión son los puntos de acceso
> público descritos para obtener una cuenta y un token. El resto de las
> operaciones requiere autenticación y puede estar restringido según el
> rol.

## Endpoints

Las rutas siguientes corresponden a las operaciones identificadas en la
API. La disponibilidad de cada operación depende de las reglas de
autorización configuradas para el rol autenticado.

### Autenticación

  --------------------------------------------------------------------------
  Método            Endpoint             Descripción       Acceso
  ----------------- -------------------- ----------------- -----------------
  `POST`            `/api/auth/signup`   Registra una      Público
                                         cuenta de         
                                         solicitante.      

  `POST`            `/api/auth/signin`   Autentica al      Público
                                         usuario y         
                                         devuelve un JWT.  
  --------------------------------------------------------------------------

### Préstamos

  ----------------------------------------------------------------------------
  Método                  Endpoint                     Descripción
  ----------------------- ---------------------------- -----------------------
  `POST`                  `/prestamos`                 Registra un préstamo
                                                       para un solicitante y
                                                       un equipo.

  `POST`                  `/prestamos/{id}/devolver`   Registra la devolución
                                                       del préstamo, calcula
                                                       la multa si corresponde
                                                       y actualiza los
                                                       estados.
  ----------------------------------------------------------------------------

#### Reglas de negocio

-   Solo se puede prestar un equipo que se encuentre en estado
    `DISPONIBLE`.
-   Al registrar el préstamo, el equipo pasa a estado `PRESTADO`.
-   El préstamo se crea con estado `ACTIVO` y una fecha de vencimiento
    calculada según el perfil del solicitante.
-   Al devolverlo, el préstamo pasa a `DEVUELTO` y el equipo vuelve a
    `DISPONIBLE`.
-   No se puede devolver dos veces el mismo préstamo.

### Reportes

  -------------------------------------------------------------------------
  Método                  Endpoint                  Descripción
  ----------------------- ------------------------- -----------------------
  `GET`                   `/reportes/vencidos`      Obtiene los préstamos
                                                    activos que ya
                                                    superaron su fecha de
                                                    vencimiento.

  `GET`                   `/reportes/activos`       Genera el reporte de
                                                    préstamos activos.

  `GET`                   `/reportes/historial`     Genera el historial de
                                                    préstamos.

  `GET`                   `/reportes/{clave}/csv`   Descarga en formato CSV
                                                    el reporte identificado
                                                    por su clave.
  -------------------------------------------------------------------------

Las claves de reporte documentadas son `vencidos`, `activos` e
`historial`.



## Códigos de respuesta HTTP

  -----------------------------------------------------------------------
  Código                  Significado             Situación habitual
  ----------------------- ----------------------- -----------------------
  `400 Bad Request`       Solicitud inválida.     Faltan campos o los
                                                  datos enviados no son
                                                  válidos.

  `401 Unauthorized`      No autenticado.         No se envió un token o
                                                  el token es inválido.

  `403 Forbidden`         Acceso denegado.        El usuario está
                                                  autenticado, pero su
                                                  rol no tiene permiso
                                                  para la operación.

  `404 Not Found`         Recurso inexistente.    No existe el equipo,
                                                  préstamo o solicitante
                                                  solicitado.

  `409 Conflict`          Conflicto con el estado Equipo ya prestado,
                          o las reglas del        préstamo ya devuelto o
                          negocio.                código repetido.
  -----------------------------------------------------------------------

## Puesta en marcha

### Requisitos

-   JDK 21.
-   Maven (o utilizar el Maven Wrapper incluido en el proyecto).

### Ejecutar la aplicación

Desde la carpeta raíz del proyecto, donde se encuentra el archivo
`pom.xml`:

En Windows:

``` powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

``` bash
./mvnw spring-boot:run
```

También se puede compilar el proyecto antes de ejecutarlo:

``` bash
./mvnw clean package
```

La aplicación se inicia en el puerto configurado en
`application.properties` (por defecto, Spring Boot utiliza el puerto
`8080`).

### Documentación interactiva

La documentación interactiva se encuentra normalmente en:

-   Swagger UI: `http://localhost:8080/swagger-ui/index.html`
-   OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Desde Swagger UI se pueden consultar los endpoints y probar las
solicitudes. Para probar operaciones protegidas, primero iniciar sesión,
copiar el JWT y utilizar el botón **Authorize** con el esquema Bearer
configurado.


## Datos de prueba

El sistema carga datos iniciales al iniciar la aplicación:

  Usuario       Contraseña        Rol
  ------------- ----------------- -------------
  `admin`       `Admin12345!`     `ADMIN`
  `encargado`   `Encargado123!`   `ENCARGADO`
  `alumno`      `alumno123`       `USER`
  `docente`     `docente123`      `USER`

También se cargan cinco equipos y un préstamo vencido asociado al equipo
`NB-003`, para facilitar las pruebas del proceso de devolución y cálculo
de multas.


## Persistencia y consideraciones

La aplicación utiliza H2 en memoria. Por lo tanto, los datos almacenados
se pierden al reiniciar el servidor y se vuelven a cargar los datos
iniciales.

Antes de desplegar la aplicación en un entorno productivo, se recomienda
configurar una base de datos persistente, gestionar las claves y
secretos mediante variables de entorno y agregar pruebas automatizadas.

Actualmente, el sistema no implementa control de concurrencia para
impedir que dos operaciones simultáneas intenten prestar el mismo
equipo. También se debe tener en cuenta que cada préstamo corresponde a
un único equipo.


