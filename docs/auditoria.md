# Auditoría funcional y técnica de PharmaSoft

## Propósito
La auditoría permite identificar quién creó o modificó un registro, qué operación importante se realizó, cuándo ocurrió y qué petición HTTP se relaciona con ella. Los accesos de auditoría se restringen al permiso administrativo `USUARIOS_ADMINISTRAR` según la actividad.

## Tres niveles de información
- **Columnas de auditoría:** campos como `creado_por`, `modificado_por` y marcas temporales en las tablas de negocio. Indican autores y fechas del registro.
- **Bitácora de auditoría:** eventos inmutables de negocio, con acción, entidad, identificador, usuario, detalle y `requestId` cuando esté disponible.
- **Logs técnicos:** información de ejecución, errores, tiempos y solicitudes. Pueden rotar y no sustituyen la bitácora persistente.

## Eventos conocidos y exigidos
| Acción | Momento de registro | Detalle útil | Evidencia |
|---|---|---|---|
| LOGIN_EXITOSO | Credenciales válidas | usuario, IP/requestId según implementación | Postman evidenció evento |
| LOGIN_FALLIDO | Autenticación rechazada | usuario intentado y contexto no sensible | Postman evidenció evento |
| VENTA_REGISTRADA | Venta confirmada | identificador, usuario, importe/código | **Pendiente**: consulta filtrada quedó vacía |
| PRECIO_MODIFICADO | Cambio de precio confirmado | producto, precio anterior y nuevo | **Pendiente**: consulta filtrada quedó vacía |
| CLIENTE_REGISTRADO | Alta de cliente confirmada | id y campos no sensibles | **Evento nuevo por implementar/verificar** |
| VENTA_ANULADA | Anulación confirmada, si existe esa operación | id y motivo no sensible | **Alternativa**; sustituir si la operación no existe |

**Segundo evento recomendado si no existe anulación:** `CATEGORIA_ELIMINADA`, después de una eliminación válida; no registrar eventos ficticios.

## Transacciones
La venta y su actualización de stock deben ser atómicas (`@Transactional`): si falla una validación no se confirma ninguna de las dos. Un evento de negocio debe emitirse o persistirse **después del commit**, por ejemplo con `@TransactionalEventListener(phase = AFTER_COMMIT)` y nueva transacción para guardar la bitácora. Esto evita presentar como exitosa una venta revertida. El login fallido no puede esperar al commit de una transacción de venta: se registra directamente desde el flujo de seguridad.

## Datos que nunca deben registrarse
Contraseñas, hashes de contraseñas, tokens JWT, secretos de configuración, números completos de tarjetas, credenciales en cabeceras, datos personales que no sean necesarios para la auditoría.

## Trazabilidad de una venta (pendiente de ejecución)
| Evidencia | Valor real |
|---|---|
| ID de venta confirmada | PENDIENTE |
| Fila de `ventas` y `creado_por` | PENDIENTE |
| Evento `VENTA_REGISTRADA` | PENDIENTE |
| `requestId` del evento | PENDIENTE |
| Línea de log con el mismo `requestId` | PENDIENTE |

Capturar las tres fuentes con el mismo identificador sin mostrar credenciales. La petición POST observada antes devolvió HTTP 400 por datos faltantes; **no se registró una venta confirmada en esa evidencia**.
