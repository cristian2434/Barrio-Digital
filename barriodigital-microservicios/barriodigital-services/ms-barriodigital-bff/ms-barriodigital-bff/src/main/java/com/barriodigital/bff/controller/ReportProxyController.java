package com.barriodigital.bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Reenvía todo lo que llegue a /api/report/** hacia ms-barriodigital-report.
 * El JWT ya fue validado por Spring Security (SecurityConfig) y esta ruta
 * solo la puede usar el rol Admin.
 * Los errores del microservicio de destino se propagan tal cual (ver ProxyHelper).
 */
@RestController
@RequestMapping("/api/report")
public class ReportProxyController {

    private final RestTemplate restTemplate;

    @Value("${services.report-url}")
    private String reportServiceUrl;

    public ReportProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/**", method = {
            RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
            RequestMethod.PATCH, RequestMethod.DELETE
    })
    public ResponseEntity<Object> proxy(@RequestBody(required = false) Object body) {
        return ProxyHelper.forward(restTemplate, "/api/report", reportServiceUrl, body);
    }
}
