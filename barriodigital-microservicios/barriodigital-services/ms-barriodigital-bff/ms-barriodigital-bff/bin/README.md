# ms-barriodigital-bff

BFF (Backend For Frontend) del sistema BarrioDigital. Valida el JWT emitido por
Azure AD (tenant `BarrioDigital`) y enruta las peticiones hacia los
microservicios de dominio (`requests`, `catalog`, etc.).

## Requisitos
- Java 17
- Maven 3.9+

## Ejecutar localmente
```bash
mvn spring-boot:run
```
Por defecto corre en el puerto **8080**.

## Configuración de Azure AD (ya cargada en `application.yml`)
- Tenant ID: `9a0d23e4-2666-4368-8276-585af06c280c`
- Client ID (audience): `bc827a00-7a89-401e-9bb3-bb9636ffbb84`
- Issuer: `https://login.microsoftonline.com/9a0d23e4-2666-4368-8276-585af06c280c/v2.0`

## Probar sin frontend (con Postman)
1. Sin token → `GET http://localhost:8080/api/me` debe devolver **401**.
2. Con un Access Token válido (obtenido con MSAL desde Angular, o con el
   flujo "Authorization Code with PKCE" de Postman usando el Client ID de
   `BarrioDigital-SPA`) → agrega el header `Authorization: Bearer <token>`
   y debe devolver tus datos de usuario y roles.

## Empaquetar para desplegar en la EC2
```bash
mvn clean package
```
Esto genera `target/ms-barriodigital-bff.jar`.

### Subir y ejecutar en la EC2 (107.23.86.85)
```bash
scp -i barriodigital-key.pem target/ms-barriodigital-bff.jar ec2-user@107.23.86.85:~/
ssh -i barriodigital-key.pem ec2-user@107.23.86.85
sudo yum install -y java-17-amazon-corretto
java -jar ms-barriodigital-bff.jar
```

## Endpoints
| Método | Ruta | Rol requerido |
|---|---|---|
| GET | `/api/me` | Cualquier usuario autenticado (diagnóstico) |
| * | `/api/requests/**` | Admin, Operador, Cliente |
| * | `/api/catalog/**` | Admin, Operador, Cliente |
| * | `/api/report/**` | Admin |
| * | `/api/audit/**` | Admin, Auditor |
| GET | `/actuator/health` | Público |
