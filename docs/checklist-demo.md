# Lista de verificación previa — Demostración PharmaSoft

**Estudiante:** Josias Pari Mamani  
**Presentación:** 19 de noviembre de 2026

Marcar las casillas únicamente después de comprobarlas en el entorno que se presentará.

## Infraestructura

- [ ] Docker Desktop funcionando.
- [ ] Contenedores `oracle-local` y `deploy-redis-1` en estado `Up` (`docker ps`).
- [ ] Oracle operativo en la PDB y esquema de desarrollo correcto.
- [ ] Backend `PhamaBackendApplication` iniciado con el perfil previsto, sin errores críticos.
- [ ] Endpoint `/actuator/health` devuelve `status: UP`.
- [ ] Frontend accesible en `http://localhost:4200` mediante `ng serve` o despliegue equivalente.
- [ ] Se confirma el perfil de entorno sin exponer contraseñas ni tokens.

## Datos y cuentas

- [ ] Cuenta ADMIN probada.
- [ ] Cuenta VENDEDOR probada.
- [ ] Cliente de demostración activo disponible.
- [ ] Producto de demostración con stock suficiente.
- [ ] Stock previo anotado y estrategia de restauración de datos comprobada.
- [ ] Pestañas de Productos, Ventas, Auditoría y reporte Playwright preparadas.
- [ ] Postman con ejemplos de 200/201, 400, 401, 403, 404 y 409 organizados.

## Flujo y evidencias

- [ ] Registrar venta de demostración y mostrar comprobante.
- [ ] Verificar stock posterior y diferencia frente al anterior.
- [ ] Verificar `CREADO_POR` en Oracle.
- [ ] Verificar evento `VENTA_REGISTRADA` de la misma venta.
- [ ] Correlacionar `requestId` entre bitácora y logs.
- [ ] Confirmar que VENDEDOR no accede a `/auditoria`.
- [ ] Revisar cambios de página y filtros sin pérdida de URL.
- [ ] Verificar que el reporte HTML de Playwright corresponde a la versión mostrada.
- [ ] Ensayo 1 realizado y cronometrado (registrar en `guion-demostracion.md`).
- [ ] Ensayo 2 realizado y cronometrado (registrar en `guion-demostracion.md`).
- [ ] Tres recorridos manuales desde estado preparado (registrar identificadores y resultados).
- [ ] Video de respaldo grabado (máximo 10 minutos) y enlace guardado.

## Plan B

Si falla internet, GitHub o el proyector: abrir documentación y capturas locales. Si se cae Oracle o Redis: explicar el punto de fallo y utilizar el video de respaldo, sin afirmar que la operación se ejecutó en vivo. Si las pruebas fallan: conservar la salida y la traza, registrar la incidencia y no ocultarla.

## Estado del checklist

**Fecha de revisión:** Pendiente  
**Responsable:** Josias Pari Mamani  
**Observaciones:** Pendiente
