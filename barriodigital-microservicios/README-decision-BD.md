# Decisión de base de datos: H2 (modo Oracle) en desarrollo

El caso BarrioDigital especifica **Oracle** como motor de base de datos para
`ms-barriodigital-requests`, `ms-barriodigital-catalog`, `ms-barriodigital-audit`
y `ms-barriodigital-report`.

Para el ciclo de desarrollo y las pruebas de esta entrega, los 4 microservicios
usan **H2 en modo de compatibilidad Oracle** (`MODE=Oracle` en la URL de
conexión) en vez de una instancia Oracle real. Esto es una decisión
intencional, no un descuido:

- **`MODE=Oracle`** hace que H2 emule el dialecto SQL de Oracle (manejo de
  secuencias, fechas, funciones propias del motor), de forma que las
  entidades JPA y las queries ya están validadas contra ese dialecto.
- **`jdbc:h2:file:./data/<nombre>db`** (en vez de `:mem:`) persiste los datos
  en disco entre reinicios del servicio, a diferencia de H2 en memoria pura.
- La capa de acceso a datos usa JPA/Hibernate estándar (entidades +
  `JpaRepository`), por lo que **migrar a Oracle real es solo un cambio de
  configuración** (`url`, `driver-class-name`, credenciales) en cada
  `application.yml` — no requiere tocar código de negocio ni repositorios.

## Cómo migrar a Oracle real

En cada `application.yml` de los 4 servicios, reemplazar:

```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/<servicio>db;MODE=Oracle;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
```

por:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@//<host>:1521/<service_name>
    driver-class-name: oracle.jdbc.OracleDriver
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

y agregar la dependencia del driver de Oracle (`ojdbc11` o equivalente) al
`pom.xml` de cada servicio, en reemplazo de la dependencia `h2`.
