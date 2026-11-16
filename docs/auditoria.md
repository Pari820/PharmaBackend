# Auditoría de PharmaSoft — Sesión 14, actividad autónoma

**Autor:** Josias Pari Mamani

## Objetivo

La bitácora permite identificar quién ejecutó una operación, qué recurso cambió y en qué solicitud HTTP ocurrió. El API es `GET /api/v1/auditoria`; se consulta con usuario administrador y admite `usuario`, `accion`, `desde`, `hasta`, `pagina` y `tamanio`.

## Eventos implementados en el backend suministrado

| Acción | Origen | Detalle guardado |
|---|---|---|
| `LOGIN_EXITOSO` | Autenticación correcta | Usuario, acción y requestId |
| `LOGIN_FALLIDO` | Autenticación incorrecta | Usuario, acción y requestId, sin contraseña |
| `VENTA_REGISTRADA` | Guardar una venta | ID, total e ítems |
| `PRECIO_MODIFICADO` | Actualizar precio | ID y datos de modificación definidos por el servicio |
| `CLIENTE_REGISTRADO` | Registrar nuevo cliente | ID, usuario y descripción genérica |
| `CLIENTE_DESACTIVADO` | Dar de baja un cliente | ID, usuario y descripción genérica |

Los dos últimos eventos se agregaron a `ClienteServiceImpl.java` como parte de la actividad autónoma. **Se deben verificar en el backend ejecutándose** antes de considerarlos aprobados.

## Diferencias

- **Columnas de auditoría:** `creado_por` y `modificado_por` describen responsables asociados al registro.
- **Bitácora:** almacena eventos de operaciones con fecha, usuario, acción, entidad, ID, detalle y requestId.
- **Logs:** registran información técnica para diagnosticar el flujo y errores; pueden rotar y no equivalen a la bitácora persistente.

## Por qué después del commit

`AuditoriaListener` escucha `EventoAuditoria` con `@TransactionalEventListener(phase = AFTER_COMMIT)`. El evento de venta/cliente se escribe solo si la transacción principal confirma. `AuditoriaServiceImpl.registrar` usa `REQUIRES_NEW` para persistirlo en otra transacción. El login fallido no modifica una entidad de negocio ni requiere un commit: se registra directamente para conservar el intento.

## Información excluida

Nunca registrar contraseñas, tokens JWT completos, secretos de firma, credenciales de Oracle, números de tarjeta ni contenido sensible de solicitudes. Evitar además el DNI, correo y dirección del cliente en el detalle: basta el ID.

## Trazabilidad pendiente de evidencia

1. Registrar una venta válida (201) y anotar su ID y requestId.
2. Consultar la venta y su columna `creado_por` en Oracle.
3. Consultar `GET /api/v1/auditoria?accion=VENTA_REGISTRADA` como ADMIN.
4. Buscar en los logs el mismo requestId.
5. Incorporar las tres capturas al informe **solo después de comprobar la coincidencia**.
