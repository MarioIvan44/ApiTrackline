# Trackline API - Sistema de Gestión y Rastreo de Órdenes de Servicio Aduanal (PTC)

![Java](https://img.shields.io/badge/Lenguaje-Java%2017-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot%203.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Seguridad-Spring%20Security%20%7C%20JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Oracle](https://img.shields.io/badge/Database-Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Heroku](https://img.shields.io/badge/Deploy-Heroku-430098?style=for-the-badge&logo=heroku&logoColor=white)

## Descripción del Proyecto

**Trackline** es un sistema para la gestión y el seguimiento de órdenes de servicio de una agencia de logística y trámites aduanales. Este repositorio contiene la **API REST** que sirve como backend central del ecosistema Trackline, consumida tanto por la aplicación web como por la aplicación móvil [TracklineAppRoutes](https://github.com/MarioIvan44/TracklineAppRoutes).

La API permite registrar una orden de servicio desde su encabezado hasta su facturación, controlar cada etapa del trámite aduanal (documentos, clasificación, digitación, registro, pago, levante, carga, en camino y entrega), asignar transportistas y viajes, y compartir el progreso del viaje en tiempo real con el cliente.

## Funcionalidades Principales

### Gestión de Órdenes de Servicio
- **Órdenes de servicio:** Creación, consulta y actualización de órdenes asociadas a un cliente mediante su NIT, con validación de pertenencia orden-cliente.
- **Encabezado de orden:** Registro de fecha, encargado, referencia, importador, NIT, registro de IVA y datos de facturación.
- **Información de embarque, recolección y aduana:** Registro del origen, destino, documento de transporte, tipo de servicio aduanal y datos del embarque.
- **Cargos y financiamiento:** Registro de montos por tipo de dato contable y tipos de financiamiento asociados a cada orden.
- **Permisos y observaciones:** Control de permisos requeridos por orden y observaciones con semáforo selectivo.

### Seguimiento del Trámite y Transporte
- **Estados del trámite:** Control por etapas (documentos, clasificación, digitación, registro, pago, levante de pago, equipo de transporte, carga, en camino, entregada y facturación).
- **Transportistas y transportes:** Administración de transportistas y unidades de transporte asignadas.
- **Viajes en tiempo real:** Registro de punto de partida, destino, coordenadas, horas estimadas y reales, además del progreso del viaje reportado desde la app móvil del transportista.

### Autenticación y Seguridad
- **Inicio de sesión con JWT en cookie:** El token se entrega en una cookie `authToken` con `HttpOnly`, `Secure` y `SameSite=None`.
- **Autorización por roles:** Cada endpoint está protegido según el rol del usuario mediante Spring Security.
- **Contraseñas con Argon2id:** Las contraseñas se almacenan con hash Argon2id.
- **Recuperación de contraseña:** Envío de un enlace por correo (Gmail SMTP) con un token temporal que abre la app mediante deep link (`trackline://reset-password`).
- **Validación centralizada:** Manejo global de errores de validación, formato de datos e integridad referencial con respuestas uniformes.

---

## Roles y Permisos

| Rol | Funciones Clave |
| :--- | :--- |
| **Administrador** | Acceso total: gestión de usuarios, empleados, clientes, transportistas y eliminación de registros sensibles. |
| **Empleado** | Operación diaria: creación y actualización de órdenes, estados, cargos, permisos, embarques, recolecciones y viajes. |
| **Transportista** | Actualización del estado del trámite y del progreso de sus viajes asignados desde la app móvil. |
| **Cliente** | Registro público de cuenta y consulta de sus órdenes y viajes para seguir su envío. |

---

## Arquitectura y Estructura del Proyecto

La API sigue una arquitectura en capas: **Controller → Service → Repository → Entity**, con DTOs para el intercambio de datos y excepciones personalizadas por módulo.

```
src/main/java/apiTrackline/proyectoPTC/
├── Config/           # Configuración de seguridad, CORS, Argon2 y beans de la aplicación
├── Controllers/      # Endpoints REST, un paquete por módulo (Aduana, Clientes, Viaje, etc.)
├── Entities/         # Entidades JPA mapeadas a las tablas de Oracle (TB_*)
├── Exceptions/       # Excepciones por módulo y manejador global de validaciones
├── Models/
│   ├── ApiResponse/  # Envoltorio estándar de respuestas
│   └── DTO/          # Objetos de transferencia de datos
├── Repositories/     # Interfaces Spring Data JPA
├── Services/         # Lógica de negocio
└── Utils/            # Utilidades JWT, filtro de autenticación por cookie y generador de contraseñas
```

### Módulos de la API

| Módulo | Ruta base | Descripción |
| :--- | :--- | :--- |
| Autenticación | `/api/auth` | Login, logout, usuario actual (`/me`) y recuperación de contraseña |
| Usuarios | `/apiUsuario` | Gestión de cuentas de usuario |
| Clientes | `/apiClientes` | Registro y gestión de clientes por NIT |
| Empleados | `/apiEmpleados` | Gestión de empleados |
| Órdenes de servicio | `/apiOrdenServicio` | Orden principal y validación orden-cliente |
| Encabezado de orden | `/apiOrden` | Datos generales y de facturación de la orden |
| Estados | `/apiEstados` | Etapas del trámite aduanal |
| Info. de embarque | `/apiInfoEmbarque` | Datos del embarque |
| Recolección | `/apiRecoleccion` | Origen, destino y documento de transporte |
| Aduana | `/apiAduana` | Tipo de servicio aduanal por orden |
| Cargos | `/apiCargos` | Montos por tipo de dato contable |
| Financiamiento | `/apiFinanciamiento`, `/apiTipoF` | Financiamientos y sus tipos |
| Permisos | `/apiPermisos`, `/apiOrdenPermisos` | Catálogo de permisos y permisos por orden |
| Observaciones | `/apiObservaciones` | Observaciones y selectivo |
| Transporte | `/apiTransporte`, `/apiTransportista`, `/apiServicioTransporte` | Unidades, transportistas y servicios de transporte |
| Viajes | `/apiViaje` | Viajes, coordenadas y progreso en tiempo real |
| Catálogos | `/apiRoles`, `/apiTipoCliente`, `/apiTipoServicio`, `/apiTipoDatoContable`, `/apiSelectivo` | Datos de referencia |

Los listados principales soportan paginación mediante los parámetros `page` y `size` (por ejemplo: `/apiViaje/datosViaje/userId/{idUsuario}?page=0&size=5`).

---

## Stack Tecnológico y Dependencias

- **Java 17:** Lenguaje principal del proyecto.
- **Spring Boot 3.5.3:** Framework base para la construcción de la API y la configuración automática del proyecto.
- **Spring Web:** Creación de controladores REST y manejo de solicitudes HTTP.
- **Spring Data JPA / Hibernate:** Mapeo objeto-relacional y acceso a datos mediante repositorios, con dialecto de Oracle.
- **Spring Validation:** Validación de los datos de entrada en los DTOs mediante anotaciones.
- **Spring Security:** Autenticación y autorización basada en roles para cada endpoint.
- **Spring Mail:** Envío de correos electrónicos para la recuperación de contraseñas mediante Gmail SMTP.
- **Oracle JDBC (ojdbc11):** Driver de conexión a la base de datos Oracle.
- **JJWT 0.11.5:** Generación y validación de JSON Web Tokens para la sesión y el restablecimiento de contraseñas.
- **Argon2-jvm:** Hash seguro de contraseñas con el algoritmo Argon2id.
- **Lombok:** Reducción de código repetitivo (getters, setters, constructores) en entidades y DTOs.
- **Dotenv-java:** Carga de variables de entorno desde un archivo `.env` para la ejecución local.
- **Cloudinary SDK:** Dependencia incluida para la gestión de imágenes en la nube (integración pendiente).
- **Maven:** Gestión de dependencias y construcción del proyecto (empaquetado `.war`).
- **Heroku:** Plataforma de despliegue de la API (`Procfile`).

---

## Configuración del Entorno y Ejecución

### Requisitos Previos
- [JDK 17](https://adoptium.net/) o superior
- Base de datos [Oracle](https://www.oracle.com/database/) (local o en la nube) con el esquema de Trackline
- Cuenta de Gmail con una [contraseña de aplicación](https://support.google.com/accounts/answer/185833) para el envío de correos
- Maven (opcional, el proyecto incluye Maven Wrapper `mvnw`)

### 1. Clonar el repositorio

```bash
git clone https://github.com/MarioIvan44/ApiTrackline.git
cd ApiTrackline
```

### 2. Configurar las variables de entorno

Copia la plantilla `.env.example` como `.env` en la raíz del proyecto y completa los valores:

```bash
# Linux / Mac
cp .env.example .env

# Windows (CMD)
copy .env.example .env
```

| Variable | Descripción |
| :--- | :--- |
| `BD_URL` | URL JDBC de Oracle, por ejemplo `jdbc:oracle:thin:@//host:1521/servicio` |
| `BD_USER` / `BD_PASSWORD` | Credenciales de la base de datos |
| `BD_DRIVER` | Driver JDBC (`oracle.jdbc.OracleDriver`) |
| `SECRET_KEY_JWT` | Clave secreta para firmar los tokens JWT |
| `SECURITY_JWT_ISSUER` | Emisor de los tokens (`apiTrackline`) |
| `JWT_EXPIRATION` | Tiempo de expiración del token en milisegundos (`86400000` = 24 h) |
| `SPRING_MAIL_USERNAME` / `SPRING_MAIL_PASSWORD` | Correo de Gmail y su contraseña de aplicación |

> El archivo `.env` se carga automáticamente al iniciar la aplicación y **no debe subirse al repositorio**.

### 3. Ejecutar la API

```bash
# Linux / Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### 4. Generar el paquete para producción

```bash
./mvnw clean package
```

Se genera el archivo `target/proyectoPTC-0.0.1-SNAPSHOT.war`, que es el que ejecuta Heroku según el `Procfile`.

---

## Convenciones de Nomenclatura

| Elemento | Convención | Ejemplo |
| :--- | :--- | :--- |
| Paquetes de controladores | `PascalCase` + sufijo `Controller` | `ViajeController`, `ClientesController` |
| Clases de controladores | `PascalCase`, nombre del módulo | `Viaje`, `Clientes`, `OrdenServicio` |
| Entidades | `PascalCase` + sufijo `Entity` | `ViajeEntity`, `OrdenServicioEntity` |
| DTOs | Prefijo `DTO` + `PascalCase` | `DTOViaje`, `DTOClientes` |
| Repositorios | `PascalCase` + sufijo `Repository` | `ViajeRepository` |
| Servicios | `PascalCase` + sufijo `Service` | `ViajeService`, `AuthService` |
| Excepciones | Prefijo `Exception` + entidad + motivo | `ExceptionViajeNoEncontrado`, `ExceptionClienteNoRegistrado` |
| Rutas base | Prefijo `api` + módulo en `PascalCase` | `/apiViaje`, `/apiOrdenServicio` |
| Endpoints | `camelCase`, verbo en español | `/agregarCliente`, `/actualizarParcialmente/{id}`, `/eliminarCargo/{id}` |
| Tablas de la base de datos | Prefijo `TB_` en mayúsculas | `TB_VIAJES`, `TB_ORDENSERVICIOS` |
| Columnas de la base de datos | `MAYÚSCULAS` sin separador | `IDVIAJE`, `HORAESTIMADALLEGADA` |
| Roles | Prefijo `ROLE_` + nombre del rol | `ROLE_Administrador`, `ROLE_Transportista` |
| Mensajes de respuesta al cliente | Siempre en **español** | `"Credenciales incorrectas"`, `"Contraseña cambiada exitosamente"` |

---

## Proyectos Relacionados

| Proyecto | Descripción |
| :--- | :--- |
| [TracklineAppRoutes](https://github.com/MarioIvan44/TracklineAppRoutes) | Aplicación Android (Kotlin) para transportistas y clientes que consume esta API |

---

## Autor

Proyecto desarrollado en equipo como parte del Proyecto Técnico Científico (PTC) del Instituto Técnico Ricaldone.

**Mario Iván Vásquez**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/mario-v%C3%A1squez-6a4948346/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/MarioIvan44)
