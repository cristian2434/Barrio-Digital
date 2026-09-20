package com.barriodigital.bff.controller;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint de diagnóstico: útil para la demo del EP2.
 * Muestra qué usuario y qué roles llegan en el JWT validado por el BFF,
 * sin depender de que los microservicios de dominio ya estén levantados.
 *
 * Pruébalo con: GET http://<IP_EC2>:8080/api/me  (o a través del API Gateway)
 * con un Bearer token válido obtenido desde el login de Angular.
 *
 * Nota: soporta tanto tokens v2 (claim "preferred_username") como v1
 * (claim "upn"), ya que Map.of() no acepta valores null y algunos
 * tenants/tokens no traen "preferred_username".
 */
@RestController
public class MeController {

    @GetMapping("/api/me")
    public Map<String, Object> me(JwtAuthenticationToken auth) {
        Jwt jwt = auth.getToken();
        List<String> roles = jwt.getClaimAsStringList("roles");

        String usuario = jwt.getClaimAsString("preferred_username");
        if (usuario == null) {
            usuario = jwt.getClaimAsString("upn");
        }
        if (usuario == null) {
            usuario = jwt.getClaimAsString("unique_name");
        }
        if (usuario == null) {
            usuario = jwt.getSubject();
        }

        String nombre = jwt.getClaimAsString("name");

        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuario);
        response.put("nombre", nombre);
        response.put("roles", roles != null ? roles : List.of());
        response.put("audience", jwt.getAudience());
        response.put("issuer", jwt.getIssuer().toString());
        response.put("authorities", auth.getAuthorities());

        return response;
    }
}