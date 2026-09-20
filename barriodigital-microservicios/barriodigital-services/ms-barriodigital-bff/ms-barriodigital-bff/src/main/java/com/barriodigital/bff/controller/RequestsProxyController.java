package com.barriodigital.bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Reenvía todo lo que llegue a /api/requests/** hacia ms-barriodigital-requests.
 * El JWT ya fue validado por Spring Security antes de llegar aquí (ver SecurityConfig).
 * Los errores del microservicio de destino se propagan tal cual (ver ProxyHelper).
 */
@RestController
@RequestMapping("/api/requests")
public class RequestsProxyController {

    private final RestTemplate restTemplate;

    @Value("${services.requests-url}")
    private String requestsServiceUrl;

    public RequestsProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/**", method = {
            RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
            RequestMethod.PATCH, RequestMethod.DELETE
    })
    public ResponseEntity<Object> proxy(@RequestBody(required = false) Object body) {
        return ProxyHelper.forward(restTemplate, "/api/requests", requestsServiceUrl, body);
    }
}
