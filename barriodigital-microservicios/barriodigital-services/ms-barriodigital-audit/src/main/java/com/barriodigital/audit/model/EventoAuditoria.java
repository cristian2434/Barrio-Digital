package com.barriodigital.audit.model;

import jakarta.persistence.*;
import java.time.Instant;

/** Persistencia de cada evento leído del tópico Kafka audit.timeline. */
@Entity
@Table(name = "eventos_auditoria")
public class EventoAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tramiteId;

    @Column(nullable = false)
    private String usuario; // quién generó el evento (preferred_username)

    @Column(nullable = false)
    private String accion; // ej. "INGRESO", "ADMISION", "VISITA", "RESOLUCION"

    @Column(nullable = false)
    private Instant fecha;

    private String detalle;

    public EventoAuditoria() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTramiteId() { return tramiteId; }
    public void setTramiteId(Long tramiteId) { this.tramiteId = tramiteId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public Instant getFecha() { return fecha; }
    public void setFecha(Instant fecha) { this.fecha = fecha; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}
