package com.barriodigital.report.listener;

import com.barriodigital.report.service.ReportService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consume el tópico "requests.events" (sección 9: 3 particiones, 3 réplicas,
 * delete, retención 3-7 días) para alimentar las agregaciones de KPIs
 * sin bloquear el core de ms-barriodigital-requests.
 */
@Component
public class RequestsEventsKafkaListener {

    private final ReportService service;

    public RequestsEventsKafkaListener(ReportService service) {
        this.service = service;
    }

    @KafkaListener(topics = "requests.events", groupId = "report-service")
    public void onEvento(Map<String, Object> payload) {
        Long tramiteId = Long.valueOf(String.valueOf(payload.get("tramiteId")));
        Long tipoTramiteId = Long.valueOf(String.valueOf(payload.get("tipoTramiteId")));
        String estado = String.valueOf(payload.get("estado"));
        service.registrarEvento(tramiteId, tipoTramiteId, estado);
    }
}
