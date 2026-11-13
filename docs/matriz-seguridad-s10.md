# Matriz de seguridad — Sesión 10

**Proyecto:** PharmaBackend  
**Rama:** `feature/seguridad-pruebas-pari`

## Parte A — 16 casos base + 4 casos propios

| ID | Grupo | Caso | Esperado | Obtenido | Estado | Evidencia |
|---|---|---|---|---|---|---|
| A-01 | Autenticación | Login admin, vendedor, almacen y supervisor | 200; roles/permisos correctos | 200 en los 4; roles/permisos correctos | Pasa | A-01 ADMIN/VENDEDOR/ALMACEN/SUPERVISOR |
| A-02 | Autenticación | Usuario inexistente y contraseña incorrecta | 401 con mismo mensaje | 401 en ambos: `Usuario o contraseña incorrectos` | Pasa | A-02A y A-02B |
| A-03 | Autenticación | Login de usuario inactivo | 401 sin revelar estado | 401: `Usuario o contraseña incorrectos` | Pasa | A-03 |
| A-04 | Autenticación | Login `VENDEDOR` en mayúsculas | 200; username case-insensitive | 200 como `vendedor` | Pasa | A-04 |
| T-01 | Token | Token sin palabra Bearer | 401 `Debe iniciar sesión...` | 401 con mensaje esperado | Pasa | T-01 |
| T-02 | Token | Carácter modificado en payload | 401 token inválido | 401 `El token no es válido` | Pasa | T-02 |
| T-03 | Token | Token emitido con secreto anterior | 401 token inválido | 401 `El token no es válido` | Pasa | T-03 |
| T-04 | Token | Token vencido a 1 minuto | 401 token expirado | 401 `El token ha expirado. Inicie sesión nuevamente` | Pasa | T-04A/T-04B |
| T-05 | Token | Revisar `sub`, `authorities`, `iat`, `exp` | Datos correctos y sin datos sensibles | Correcto; password ausente | Pasa | T-05 Visualization |
| P-01 | Permisos | VENDEDOR POST/PUT/DELETE categorías | 403 en los tres | 403 en los tres | Pasa | P-01A/B/C |
| P-02 | Permisos | ALMACENERO GET clientes y POST ventas | 403 en ambos | 403 en ambos | Pasa | P-02A/B |
| P-03 | Permisos | VENDEDOR y ALMACENERO acceden a reporte | 403 ambos | 403 ambos | Pasa | P-03A/B |
| P-04 | Permisos | ADMIN accede a cada módulo | 2xx todas | 200 en productos, clientes, ventas, usuarios y reportes | Pasa | P-04A-E |
| U-01 | Usuarios | Password `abcdefgh` y `rolIds: []` | 400 en ambos | 400 en ambos | Pasa | U-01A/B |
| U-02 | Usuarios | Duplicado y luego crear María Huamán | 409 y 201; sin password; BCrypt; rol asociado | 409 y 201; hash `$2a$10$...`; rol VENDEDOR | Pasa | U-02A/B + SQL |
| C-01 | CORS | SPA localhost:4200, OPTIONS + GET | OPTIONS 200 y real 200/401, sin CORS error | OPTIONS 200 y GET 200 | Pasa | Network OPTIONS/GET |
| X-01 | Autenticación | Username vacío | 400 | 400 `El usuario es obligatorio` | Pasa | X-01 |
| X-02 | Token | Token `abc.def.ghi` | 401 | 401 `El token no es válido` | Pasa | X-02 |
| X-03 | Permisos | VENDEDOR consulta productos | 200 | 200 | Pasa | X-03 |
| X-04 | Usuarios | Asignar roles a usuario 9999 | 404 | 404 `Usuario no encontrado con id: 9999` | Pasa | X-04 |

## Parte B — Casos B-01 a B-08

| ID | Parte | Caso | Esperado | Obtenido | Estado | Evidencia |
|---|---|---|---|---|---|---|
| B-01 | B1 | Cambio de contraseña correcto | 204 | 204 | Pasa | B-01 |
| B-02 | B1 | Contraseña actual incorrecta | 409 | 409 con mensaje esperado | Pasa | B-02 |
| B-03 | B1 | Nueva contraseña igual a la actual | 409 | 409 con mensaje esperado | Pasa | B-03 |
| B-04 | B1 | Nueva contraseña inválida | 400 | 400 | Pasa | B-04 |
| B-05 | B2 | Asignar VENDEDOR + ALMACENERO y reutilizar el mismo JWT | 200 y nuevo permiso inmediato | 200; mismo token pudo hacer PUT producto | Pasa | B-05/B-05B |
| B-06 | B2 | Roles vacíos, rol inexistente y auto-remoción de ADMIN | 400, 404 y 409 | 400, 404 y 409 | Pasa | B-06/B-07/B-08 |
| B-07 | B3 | Desactivar vendedor; token existente y login dejan de funcionar | 200, luego 401 y 401 | Resultados esperados | Pasa | B3-01/B3-02/B3-03 |
| B-08 | B3 | ADMIN no puede desactivarse; reactivar vendedor | 409, luego 200; login 200 | Resultados esperados | Pasa | B3-04/B3-05/login |

## Resumen

- Casos base: **16/16 pasan**.
- Casos propios: **4/4 pasan**.
- Casos B-01 a B-08: **8/8 pasan**.
- Total documentado: **28 casos**.
- Fallos funcionales detectados en la ejecución final: **0**.
