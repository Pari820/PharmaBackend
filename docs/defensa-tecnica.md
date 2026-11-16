# Defensa técnica — Preguntas y respuestas del Proyecto Integrador

**Estudiante:** Josias Pari Mamani

> Guía para estudiar y adaptar al código realmente desplegado. Corroborar las rutas Java/TypeScript, porque la versión local subida anteriormente puede ser distinta del último commit.

### 1. ¿Por qué la paginación se realiza en el servidor?
Oracle devuelve solo la porción requerida, por ejemplo 20 elementos. Así el navegador no recibe miles de filas ni tiene que paginarlas en memoria. El backend utiliza consultas paginadas mediante Spring Data JPA, que normalmente ejecutan una consulta del contenido y otra de conteo cuando la paginación necesita el total. **Código a revisar:** `ProductoServiceImpl.java`, repositorio de productos y listado Angular.

### 2. ¿Cómo evitas ventas incompletas y eventos de auditoría falsos?
La creación de la venta y la reducción de stock deben quedar dentro de una transacción `@Transactional`. Si hay una excepción, la transacción se revierte. El evento de auditoría se registra luego del commit (`@TransactionalEventListener(AFTER_COMMIT)`) para no confirmar acciones fallidas. **Código:** `VentaServiceImpl.java`, `AuditoriaListener.java`. **Evidencia:** venta 63 y su evento de auditoría, corroborados con `requestId 3904ec86`.

### 3. ¿Qué ocurre si el token vence en medio de una venta?
La API debe devolver 401. El interceptor de autenticación/error del frontend detecta la respuesta, limpia la sesión y redirige al login; se debe comprobar si conserva la URL de retorno y si evita una venta duplicada al reintentar. **Código:** `core/interceptors/` y guards. **Prueba:** E2E con token vencido o alterado.

### 4. ¿Por qué se invalida la caché de productos después de una venta?
Porque una venta reduce el stock. Si la caché mantiene la versión previa, el catálogo mostraría unidades que ya no están disponibles. El servicio de venta debe invalidar las claves relevantes tras la confirmación. **Código a revisar:** servicios de Venta/Producto y configuración de Redis.

### 5. ¿Qué diferencia existe entre log, columna de auditoría y bitácora?
El log diagnostica la ejecución, con información temporal. Las columnas `creado_por`/`modificado_por` reflejan quién realizó cambios en filas específicas. La bitácora conserva eventos de negocio consultables, vinculando acción, usuario, entidad y petición. **Código:** entidades de negocio, `AuditoriaEvento.java`, controlador de auditoría.

### 6. ¿Cómo sabes que una prueba E2E es estable?
La corro tres veces sin reintentos artificiales ni esperas fijas. Debe superar las mismas aserciones y dejar datos consistentes. Si falla de forma intermitente, reviso Playwright trace y documento la causa antes de corregir. **Código:** archivos `*.spec.ts` de Playwright y configuración de pruebas. **Evidencia:** 13 pruebas, 3 repeticiones, 39/39 exitosas en 47,2 s, commit Frontend `53a596b`.

### 7. ¿Por qué se utiliza un DTO y `@Valid`?
El DTO define el contrato de entrada y sus restricciones (`@NotBlank`, `@NotNull`, `@Positive`). `@Valid` en el controller rechaza datos mal formados antes de registrar un libro/producto/venta. Las restricciones de negocio se verifican también en el servicio. **Código:** DTO de ventas y controller correspondiente.

### 8. ¿Por qué el vendedor recibe HTTP 403 cuando consulta auditoría?
El token identifica al usuario, pero su rol no tiene autorización para el recurso. El backend responde 403 por el filtro de Spring Security; esconder una opción del menú Angular no sería suficiente por sí solo. **Código:** `SecurityConfig.java` y `permisoGuard` Angular. **Evidencia:** Postman 403 vendedor.

### 9. ¿Cómo se mantienen filtros y paginación al recargar la página?
La página y el tamaño se guardan en parámetros de consulta de la URL. Angular los lee nuevamente al cargar y vuelve a consultar al servidor. Al cambiar filtros se reinicia la página a cero. **Código:** listados de Productos/Ventas y componente paginador compartido.

### 10. ¿Qué función cumplen Flyway y las migraciones?
Versionan los cambios estructurales de Oracle. Las migraciones crean o modifican objetos con un orden repetible; `flyway_schema_history` registra qué versiones se aplicaron. **Código:** `src/main/resources/db/migration/`, migraciones V3/V4 y configuración Flyway.


## Referencias concretas a código y evidencias de esta entrega

- `src/main/java/pe/edu/upeu/PharmaBackend/service/impl/VentaServiceImpl.java`: transacción de registro, actualización de stock, caché de productos y publicación del evento.
- `src/main/java/pe/edu/upeu/PharmaBackend/auditoria/AuditoriaListener.java`: `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`.
- `src/main/java/pe/edu/upeu/PharmaBackend/config/RequestIdFilter.java`: `X-Request-Id`, MDC y log de peticiones.
- `src/main/java/pe/edu/upeu/PharmaBackend/config/SecurityConfig.java`: autorización HTTP.
- `src/main/java/pe/edu/upeu/PharmaBackend/service/impl/ClienteServiceImpl.java`: dos eventos nuevos, commit Backend `3cfbf31`.
- `pharma-frontend/e2e/funcionalidades.spec.ts`, `inicio.spec.ts`, `seguridad.spec.ts` y `playwright.config.ts`: suite y configuración E2E.
- `docs/evidencias/s14-estabilizacion/venta63-*.png`: demostración de registro de la venta 63.
