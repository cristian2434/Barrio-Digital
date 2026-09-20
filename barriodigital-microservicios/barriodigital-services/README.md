# Microservicios de dominio — BarrioDigital

Estos 5 microservicios, sumados al `ms-barriodigital-bff` (proyecto aparte),
completan el backend según la sección 5 del caso.

| Servicio | Puerto | Estructura | BD / Cola |
|---|---|---|---|
| `ms-barriodigital-requests` | 8081 | controller, dto, model, repository, service, exception | H2 (→ Oracle en prod) |
| `ms-barriodigital-catalog` | 8082 | controller, dto, model, repository, service, exception | H2 (→ Oracle en prod) |
| `ms-barriodigital-notify` | 8085 | dto, service, listener (+ RabbitConfig) | Sin BD — RabbitMQ |
| `ms-barriodigital-audit` | 8084 | controller, dto, model, repository, service, listener | H2 (→ Oracle) + Kafka |
| `ms-barriodigital-report` | 8083 | controller, dto, model, repository, service, listener | H2 (→ Oracle) + Kafka |

## Por qué `notify` no tiene `controller` ni `model`
El caso lo especifica explícitamente: *"no público (consumidor RabbitMQ)"*.
No expone API REST, así que no necesita `controller`. Tampoco persiste nada
(*"sin DB"*), así que no necesita `model`/`repository`. Su entrada son los
`@RabbitListener` en `listener/`, y su "estructura base" equivalente es:
`dto` (el envelope del mensaje) + `service` (qué hacer con él) + `listener`
(cómo se recibe).

## Cómo correr cada uno en tu máquina
```bash
cd ms-barriodigital-requests
mvn spring-boot:run
```
Cada uno usa H2 en memoria por defecto — no necesitas instalar nada más para
probarlos aislados. `notify`, `audit` y `report` necesitan RabbitMQ/Kafka
corriendo para sus listeners (los levantaremos más adelante con Docker
Compose, sección 7 del caso); mientras tanto puedes correrlos igual, solo
que sus colas/tópicos no van a tener nada que consumir.

## Endpoints con datos reales (para probar con Postman)
```
POST http://localhost:8082/api/catalog/procedures
{ "nombre": "Certificado de residencia", "requisitos": "Cédula", "cupoDiarioTotal": 20 }

POST http://localhost:8081/api/requests
{ "tipoTramiteId": 1, "descripcion": "Necesito certificado para trámite bancario" }

PUT http://localhost:8081/api/requests/1/status
{ "status": "ADMITIDO" }
```

## Conexión con el BFF
El `ms-barriodigital-bff` reenvía hacia estos servicios usando las URLs
configuradas en su propio `application.yml` (`services.requests-url`,
`services.catalog-url`, etc.) — por defecto apuntan a `localhost` con estos
mismos puertos, para correrlos todos juntos en tu máquina antes de subirlos
a la EC2.

## Siguiente paso (fuera de este entregable)
- Reemplazar H2 por el driver de Oracle en `application.yml` cuando tengas
  la BD cloud lista.
- Levantar RabbitMQ y Kafka (Docker Compose, sección 7 del caso) para que
  `notify`, `audit` y `report` reciban mensajes reales.
- Hacer que `ms-barriodigital-requests` publique en Kafka (`requests.events`)
  al cambiar de estado, y en RabbitMQ al admitir/resolver un trámite — están
  marcados como `// TODO` en `TramiteService.java`.
