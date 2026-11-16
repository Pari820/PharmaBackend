# Guion de demostración — PharmaSoft

**Estudiante:** Josias Pari Mamani  
**Curso:** Lenguaje de Programación II · Unidad III · Actividad Autónoma, Sesión 14  
**Presentación:** 19 de noviembre de 2026  
**Duración objetivo:** 10 minutos

## 0:00–1:00 | Contexto y arquitectura

«PharmaSoft es un sistema de botica que administra clientes, productos, inventario y ventas. Está construido con Angular para la interfaz, Spring Boot para la API REST, Oracle como base de datos y Redis para caché. Las operaciones sensibles están protegidas por autenticación JWT y permisos. La paginación se ejecuta en el servidor para que las consultas no carguen todos los registros en el navegador».

**En pantalla:** una lámina de arquitectura: Angular → API Spring Boot (JWT, servicios, auditoría) → Oracle; Redis junto al backend. Identificar el entorno de desarrollo, sin mostrar contraseñas.

## 1:00–4:30 | Flujo principal

«Ingreso con el perfil VENDEDOR. Consulto un cliente activo y un producto con stock. Al registrar una venta, el backend valida disponibilidad y persiste la operación. El sistema genera un comprobante que identifica el pedido, sus productos y su importe».

**En pantalla:** ingresar como vendedor; Clientes; Productos; crear una venta de **una unidad** con datos de demostración; abrir comprobante; volver a Productos para ver el stock actualizado. Registrar stock **antes y después** para acreditar la reducción. Evitar repetir el POST de una venta aprobada.

**Respaldo ya comprobado:** venta **63**, cliente **Ana Álvarez**, producto **Naproxeno 550 mg**, cantidad **1**, total **S/ 12,00**, usuario **vendedor**, estado `REGISTRADA`. La captura posterior del inventario indica **19 unidades**; no existe evidencia previa suficiente para asegurar que tenía 20.

## 4:30–6:00 | Seguridad

«El menú se adapta a los permisos del usuario. Como VENDEDOR no puedo abrir Auditoría. Aunque intente introducir la URL directamente, el guard envía a la pantalla de acceso denegado. El backend también protege los endpoints: sin autenticación devuelve 401 y con permisos insuficientes devuelve 403».

**En pantalla:** intentar `/auditoria` con vendedor → `/acceso-denegado`; evidencias Postman de 401 y 403. Explicar que ocultar un elemento de Angular no sustituye la autorización de Spring Security.

## 6:00–7:30 | Paginación, caché y salud

«Los listados de Productos y Ventas solicitan al servidor solo la página necesaria. Los parámetros se reflejan en la URL y se conservan al recargar. Redis contribuye a la caché de consultas y Actuator permite verificar el estado general del backend».

**En pantalla:** Productos y Ventas; cambiar tamaño de página; revisar parámetros `pagina` y `tamanio`; mostrar `GET /actuator/health` → `UP`; señalar Redis en Docker. **No afirmar que hay 5 000 productos**, ni mostrar cifras de mejora de rendimiento, salvo que estén comprobadas con datos y medidas reales.

## 7:30–8:30 | Auditoría y trazabilidad

«La bitácora registra acciones relevantes después de completarse correctamente la operación. Para la venta 63 aparecen la acción `VENTA_REGISTRADA`, el usuario vendedor y el identificador de petición `3904ec86`. Ese mismo identificador aparece en los logs de Spring Boot».

**En pantalla:** `/auditoria` como ADMIN con filtro `VENTA_REGISTRADA`; registro de ID 63; consulta Oracle `SELECT * FROM VENTAS WHERE ID=63` con `CREADO_POR=vendedor`; logs con `[3904ec86] Auditoria VENTA_REGISTRADA | Venta 63 | usuario=vendedor` y `POST /api/v1/ventas -> 201 (298 ms)`.

## 8:30–10:00 | Calidad y cierre

«Automatizamos 13 casos en Chromium para verificar login, permisos, auditoría, filtros, paginación y validaciones. Ejecutamos la suite tres veces, sin reintentos y con un trabajador: 39 ejecuciones aprobadas en 47,2 segundos. Una prueba de filtrado falló antes de la corrección porque la tabla cambiaba mientras el test recorría sus filas. Se corrigió esperando el estado correcto en lugar de usar índices inestables».

**En pantalla:** reporte HTML de Playwright (39 passed, 0 failed, 0 flaky); `docs/incidencias.md`. Explicar los límites: la suite actual no demuestra por sí sola todos los escenarios opcionales de la guía; la demostración manual y el video son comprobaciones adicionales.

## Ensayos cronometrados (realizar al menos dos)

| Ensayo | Fecha y hora | Duración real | Desvíos | Ajuste realizado |
|---|---|---|---|---|
| 1 | Pendiente | Pendiente | Pendiente | Pendiente |
| 2 | Pendiente | Pendiente | Pendiente | Pendiente |

## Verificación manual del flujo principal (tres recorridos)

Usar datos de prueba conocidos y restaurar el estado antes de cada recorrido, sin manipular historiales reales ni borrar evidencias. Anotar identificadores distintos de ventas y stock inicial/final. **La venta 63 es una evidencia previa; no cuenta como tres recorridos nuevos desde una base recién preparada.**

| Recorrido | Fecha y hora | Duración | Cliente / producto | Stock antes → después | Venta y resultado / desvíos |
|---|---|---|---|---|---|
| 1 | Pendiente | Pendiente | Pendiente | Pendiente | Pendiente |
| 2 | Pendiente | Pendiente | Pendiente | Pendiente | Pendiente |
| 3 | Pendiente | Pendiente | Pendiente | Pendiente | Pendiente |

## Video de respaldo

**Archivo o enlace:** Pendiente. Grabar máximo 10 minutos. No mostrar credenciales, tokens completos, secretos ni datos personales innecesarios. Agregar el enlace definitivo al informe y a `docs/README.md`.

## Evidencias disponibles

- `docs/evidencias/s14-estabilizacion/venta63-comprobante.png`
- `docs/evidencias/s14-estabilizacion/venta63-auditoria.png`
- `docs/evidencias/s14-estabilizacion/venta63-oracle.png`
- `docs/evidencias/s14-estabilizacion/venta63-stock.png`
- `docs/evidencias/s14-estabilizacion/venta63-logs.png`
- `docs/evidencias/s14-estabilizacion/playwright-reporte-resumen.png`
- `docs/evidencias/s14-estabilizacion/playwright-reporte-detalle.png`
