# Arquitectura PharmaSoft

```mermaid
flowchart LR
  V[Usuarios administrador / vendedor] --> A[SPA Angular]
  A -->|HTTP + JWT| B[Spring Boot REST]
  B -->|JPA / SQL| O[(Oracle)]
  B -->|caché de consultas| R[(Redis)]
  B -->|eventos luego del commit| U[(auditoria_eventos)]
  B -->|logs requestId| L[Registro del servidor]
  E[Playwright] --> A
  P[Postman] --> B
```

**Exportación pendiente:** convertir el diagrama a PNG con Mermaid Live y guardarlo como `docs/arquitectura.png`. Los componentes de Redis y los logs deben contrastarse con la configuración actual.
