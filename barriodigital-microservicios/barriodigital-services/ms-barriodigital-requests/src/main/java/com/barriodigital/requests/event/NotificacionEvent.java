package com.barriodigital.requests.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Espejo de com.barriodigital.notify.dto.NotificacionEvent.
 * No se reutiliza la clase de ms-barriodigital-notify porque son dos
 * microservicios/JARs independientes; lo que importa es que el JSON
 * resultante tenga los mismos nombres de campo (ver MessagingConfig,
 * que usa TypePrecedence.INFERRED del lado de notify para no depender
 * del nombre de clase Java del emisor).
 */
public class NotificacionEvent {
    private String type;
    private String eventId = UUID.randomUUID().toString();
    private Instant timestamp = Instant.now();
    private String traceId;
    private String correlationId;
    private Long tramiteId;
    private String destinatario;
    private String mensaje;

    public NotificacionEvent() {
    }

    public NotificacionEvent(String type, Long tramiteId, String destinatario, String mensaje) {
        this.type = type;
        this.tramiteId = tramiteId;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.correlationId = "tramite-" + tramiteId;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public Long getTramiteId() { return tramiteId; }
    public void setTramiteId(Long tramiteId) { this.tramiteId = tramiteId; }

    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
