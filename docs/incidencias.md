# Registro de incidencias — PharmaSoft

**Proyecto:** PharmaSoft · Lenguaje de Programación II · Sesión 14  
**Estudiante:** Josias Pari Mamani  
**Rama:** `feature/integracion-estabilizacion-pari`  
**Corte del registro:** 16/11/2026

> Los commits citados se tomaron del historial de Git mostrado en el repositorio. Donde no hay evidencia de un defecto del producto, se identifica explícitamente como incidencia de pruebas o de preparación. La asignación de un commit no significa que una petición fallida haya sido reparada en ese commit.

## INC-S14-01 — Paginación: persistencia del tamaño de página

- **Fecha:** durante la implementación de la sesión 14 (fecha exacta por confirmar en Git).
- **Módulo:** Frontend / Ventas.
- **Severidad:** Media.
- **Descripción y reproducción:** seleccionar un tamaño de página en el listado de Ventas y actualizar el navegador. Durante la integración se identificó que el selector de tamaño debía sincronizarse con el parámetro de la URL.
- **Esperado:** conservar el tamaño seleccionado tras recargar.
- **Obtenido inicialmente:** selector visual no sincronizado con el tamaño de la URL.
- **Causa raíz:** opción del selector sin sincronización correcta con el valor actual.
- **Corrección:** sincronizar la opción seleccionada con el tamaño actual mediante la condición `[selected]="t === tamanio()"` del paginador reutilizable.
- **Commit relacionado:** implementación de sesión 14 en Frontend; localizar el hash exacto con `git log --oneline -- src/app` antes de la entrega.
- **Prueba de regresión:** comprobación manual de parámetros de Ventas al recargar; falta adjuntar automatización específica de Ventas.
- **Estado:** Corregida funcionalmente; vinculación del commit exacto pendiente.

## INC-S14-02 — Pantalla de auditoría ausente en la SPA

- **Fecha:** 15/11/2026.
- **Módulo:** Frontend / Auditoría.
- **Severidad:** Media.
- **Descripción y reproducción:** intentar abrir `/auditoria` antes de la ampliación autónoma.
- **Esperado:** tabla paginada con filtros y acceso limitado a usuarios autorizados.
- **Obtenido inicialmente:** funcionalidad pendiente de implementar en Angular; el backend ya exponía la consulta.
- **Causa raíz:** endpoint de auditoría implementado sin vista correspondiente en la SPA.
- **Corrección:** incorporar ruta, componente, filtros, paginador y menú protegido por permiso.
- **Commit relacionado:** el archivo actualizado del Frontend todavía debe vincularse al commit que lo incluya. El commit `53a596b` incorpora las pruebas automatizadas, no constituye por sí solo evidencia del cambio de pantalla.
- **Prueba de regresión:** E2E-07, E2E-08, E2E-09, E2E-10, E2E-12; comprobación manual ADMIN y VENDEDOR.
- **Estado:** Funcionalidad verificada manualmente y por Playwright; confirmar commit de código antes de cerrar documentalmente.

## INC-S14-03 — Eventos de clientes pendientes en bitácora

- **Fecha:** 15/11/2026.
- **Módulo:** Backend / Auditoría.
- **Severidad:** Media.
- **Descripción y reproducción:** registrar o desactivar un cliente en la versión anterior y revisar el filtro de acción de la bitácora.
- **Esperado:** eventos `CLIENTE_REGISTRADO` y `CLIENTE_DESACTIVADO` después de confirmar la operación.
- **Obtenido inicialmente:** estos dos eventos adicionales no estaban incorporados.
- **Causa raíz:** faltaban las llamadas de publicación de eventos en el flujo de clientes.
- **Corrección:** integrar ambos eventos en `ClienteServiceImpl.java`.
- **Commit de corrección:** `3cfbf31` — `feat(s14): auditar registro y desactivacion de clientes`.
- **Prueba de regresión:** comprobación manual de ambos eventos para el cliente 8 y filtro de acciones en Auditoría; automatización específica de ambos registros pendiente.
- **Estado:** Cerrada, con evidencias manuales de ambos eventos.

## INC-S14-04 — Credenciales de ejemplo en pruebas E2E

- **Fecha:** 16/11/2026.
- **Módulo:** Pruebas / Autenticación.
- **Severidad:** Media.
- **Descripción y reproducción:** ejecutar la suite de 13 pruebas tras configurar las variables `E2E_ADMIN_PASSWORD` y `E2E_VENDEDOR_PASSWORD` con textos de ejemplo.
- **Esperado:** autenticación válida y acceso a los módulos protegidos.
- **Obtenido:** siete pruebas quedaron en `/login`; seis aprobaron (6/13).
- **Causa raíz:** variables de entorno de pruebas sin credenciales reales válidas. No es evidencia de fallo del mecanismo JWT.
- **Corrección:** definir las credenciales locales correctas sin escribirlas en el repositorio.
- **Commit relacionado:** `53a596b` (suite E2E; la corrección de variables fue operacional y no generó commit).
- **Prueba de regresión:** ejecución posterior 13/13 y luego 39/39.
- **Estado:** Cerrada (configuración de pruebas).

## INC-S14-05 — Fluctuación por lectura de filas durante filtrado

- **Fecha:** 16/11/2026.
- **Módulo:** Pruebas / Auditoría E2E-08.
- **Severidad:** Media.
- **Descripción y reproducción:** ejecutar `npx playwright test --repeat-each=3 --reporter=list,html`; E2E-08 falla ocasionalmente al leer `tbody tr` con `.nth(i)` tras aplicar filtro.
- **Esperado:** verificar el filtro de acción y que su valor se conserve en la URL.
- **Obtenido:** 38 aprobadas y 1 fallida por tiempo de espera de 30 segundos.
- **Causa raíz:** recorrido por índices de una tabla que se actualiza de forma asíncrona durante el filtrado.
- **Corrección:** cambiar el recorrido por una espera explícita de la acción filtrada y una comprobación declarativa de filas no coincidentes.
- **Commit de corrección:** `53a596b` — `test(s14): agregar pruebas E2E y corregir estabilidad de auditoria`.
- **Prueba de regresión:** `npx playwright test --repeat-each=3 --reporter=list,html`: 39 aprobadas, 0 fallidas, 47,2 s. Capturas: `docs/evidencias/s14-estabilizacion/playwright-reporte-resumen.png` y `playwright-reporte-detalle.png`.
- **Estado:** Cerrada.

## INC-S14-06 — Solicitud Postman de venta incompleta

- **Fecha:** 13/11/2026.
- **Módulo:** Pruebas API / Ventas.
- **Severidad:** Baja.
- **Descripción y reproducción:** enviar `POST` de registro de venta con `clienteId` y/o `detalles[0].productoId` faltantes.
- **Esperado:** validar los campos requeridos y devolver HTTP 400 sin registrar la venta.
- **Obtenido:** HTTP 400 con `validationErrors` para ambos campos.
- **Causa raíz:** cuerpo JSON de prueba incompleto. Es una incidencia de preparación de datos, no un defecto confirmado del backend.
- **Corrección:** preparar un JSON válido con un cliente existente, producto existente y stock suficiente, y volver a ejecutar una sola vez para evitar descuentos repetidos.
- **Commit relacionado:** no aplica: no se modificó código para este rechazo, que es correcto.
- **Prueba de regresión:** captura Postman de HTTP 400; la venta 63 fue comprobada posteriormente: `CREADO_POR=vendedor`, HTTP 201 y evento `VENTA_REGISTRADA`, requestId `3904ec86`.
- **Estado:** Cerrada como prueba negativa; **venta 63 confirmada mediante comprobante, Oracle, evento y logs**.

## Resumen

| Severidad | Cantidad |
|---|---:|
| Crítica | 0 |
| Alta | 0 |
| Media | 5 |
| Baja | 1 |
| **Total** | **6** |

| Módulo | Cantidad |
|---|---:|
| Frontend / Ventas | 1 |
| Frontend / Auditoría | 1 |
| Backend / Auditoría | 1 |
| Pruebas / Autenticación | 1 |
| Pruebas / Auditoría | 1 |
| Pruebas API / Ventas | 1 |

**Incidencias críticas o altas abiertas:** 0.  
**Pendientes documentales:** precisar commits de INC-S14-01 e INC-S14-02 y el registro de la venta 63 ya está trazado; queda evidenciar el stock anterior.  
**Criterio de cierre:** las incidencias de configuración de pruebas se consideran cerradas cuando su ejecución posterior es exitosa; las verificaciones funcionales pendientes no se presentan como pruebas aprobadas.
