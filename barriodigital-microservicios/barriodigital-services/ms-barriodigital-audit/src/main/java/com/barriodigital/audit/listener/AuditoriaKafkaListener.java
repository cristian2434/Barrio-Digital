package com.barriodigital.audit.listener;

import com.barriodigital.audit.service.AuditoriaService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consume el tópico "audit.timeline" (sección 9 del caso: 3 particiones,
 * 3 réplicas, política compact+delete, retención 14-30 días).
 * Se espera un JSON simple con tramiteId, usuario, accion y detalle.
 */
@Component
public class AuditoriaKafkaListener {

    private final AuditoriaService service;

    public AuditoriaKafkaListener(AuditoriaService service) {
        this.service = service;
    }

    @KafkaListener(topics = "audit.timeline", groupId = "audit-service")
    @SuppressWarnings("unchecked")
    public void onEvento(Map<String, Object> payload) {
        Long tramiteId = Long.valueOf(String.valueOf(payload.get("tramiteId")));
        String usuario = String.valueOf(payload.get("usuario"));
        String accion = String.valueOf(payload.get("accion"));
        String detalle = String.valueOf(payload.getOrDefault("detalle", ""));
        service.registrar(tramiteId, usuario, accion, detalle);
    }
}
