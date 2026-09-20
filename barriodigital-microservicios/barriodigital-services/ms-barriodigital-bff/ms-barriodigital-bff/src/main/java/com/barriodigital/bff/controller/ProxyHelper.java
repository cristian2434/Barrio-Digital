package com.barriodigital.bff.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Lógica compartida de reenvío hacia los microservicios de dominio.
 * Si el microservicio responde con error (4xx/5xx), se propaga tal cual
 * al cliente en vez de convertirlo en un 500 genérico.
 */
final class ProxyHelper {

    private ProxyHelper() {
    }

    static ResponseEntity<Object> forward(RestTemplate restTemplate,
                                           String basePath,
                                           String targetServiceUrl,
                                           Object body) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String path = request.getRequestURI().replaceFirst(basePath, "");
        String targetUrl = targetServiceUrl + basePath + path;
        if (request.getQueryString() != null) {
            targetUrl += "?" + request.getQueryString();
        }

        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        HttpEntity<Object> entity = new HttpEntity<>(body);

        try {
            return restTemplate.exchange(targetUrl, method, entity, Object.class);
        } catch (RestClientResponseException ex) {
            // Reenvía el status y el body de error reales del microservicio
            // (ej. 404 si el trámite no existe, 400 si la validación falla),
            // en vez de dejar que Spring lo transforme en un 500 genérico.
            HttpStatusCode status = HttpStatusCode.valueOf(ex.getStatusCode().value());
            return ResponseEntity.status(status).body(ex.getResponseBodyAsString());
        }
    }
}
