package com.barriodigital.requests.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * "El cupo diario disminuye al admitir el trámite" (sección 3 del caso).
 * Llamada síncrona a ms-barriodigital-catalog: PUT /api/catalog/procedures/{id}/descontar-cupo
 */
@Component
public class CatalogClient {

    private static final Logger log = LoggerFactory.getLogger(CatalogClient.class);

    private final RestTemplate restTemplate;
    private final String catalogBaseUrl;

    public CatalogClient(RestTemplate restTemplate, @Value("${catalog.base-url}") String catalogBaseUrl) {
        this.restTemplate = restTemplate;
        this.catalogBaseUrl = catalogBaseUrl;
    }

    /**
     * @return true si se descontó el cupo correctamente, false si catalog no está
     *         disponible o el cupo ya está agotado (no debe bloquear la admisión del
     *         trámite por un problema de infraestructura, pero sí queda logueado).
     */
    public boolean descontarCupo(Long tipoTramiteId) {
        String url = catalogBaseUrl + "/api/catalog/procedures/" + tipoTramiteId + "/descontar-cupo";
        try {
            restTemplate.put(url, null);
            log.info("Cupo descontado en catalog para tipoTramiteId {}", tipoTramiteId);
            return true;
        } catch (RestClientException ex) {
            log.error("No se pudo descontar cupo en catalog para tipoTramiteId {}: {}", tipoTramiteId, ex.getMessage());
            return false;
        }
    }
}
