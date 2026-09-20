package com.barriodigital.audit.dto;

import com.barriodigital.audit.model.EventoAuditoria;
import java.time.Instant;

public class EventoAuditoriaResponse {
    private Long id;
    private Long tramiteId;
    private String usuario;
    private String accion;
    private Instant fecha;
    private String detalle;

    public static EventoAuditoriaResponse fromEntity(EventoAuditoria e) {
        EventoAuditoriaResponse r = new EventoAuditoriaResponse();
        r.id = e.getId();
        r.tramiteId = e.getTramiteId();
        r.usuario = e.getUsuario();
        r.accion = e.getAccion();
        r.fecha = e.getFecha();
        r.detalle = e.getDetalle();
        return r;
    }

    public Long getId() { return id; }
    public Long getTramiteId() { return tramiteId; }
    public String getUsuario() { return usuario; }
    public String getAccion() { return accion; }
    public Instant getFecha() { return fecha; }
    public String getDetalle() { return detalle; }
}
