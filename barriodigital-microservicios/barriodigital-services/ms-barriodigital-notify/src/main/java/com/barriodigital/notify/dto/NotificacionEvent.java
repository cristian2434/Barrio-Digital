package com.barriodigital.notify.dto;

import java.time.Instant;

/**
 * Envelope común para los mensajes de las 3 colas (buena práctica sección 8):
 * type, eventId, timestamp, traceId, correlationId + payload.
 */
public class NotificacionEvent {
    private String type;
    private String eventId;
    private Instant timestamp;
    private String traceId;
    private String correlationId;
    private Long tramiteId;
    private String destinatario;
    private String mensaje;

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
