package com.barriodigital.report.model;

import jakarta.persistence.*;
import java.time.Instant;

/** Copia local (para agregaciones) de cada evento leído del tópico requests.events. */
@Entity
@Table(name = "eventos_tramite")
public class EventoTramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tramiteId;

    @Column(nullable = false)
    private Long tipoTramiteId;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Instant fecha;

    public EventoTramite() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTramiteId() { return tramiteId; }
    public void setTramiteId(Long tramiteId) { this.tramiteId = tramiteId; }

    public Long getTipoTramiteId() { return tipoTramiteId; }
    public void setTipoTramiteId(Long tipoTramiteId) { this.tipoTramiteId = tipoTramiteId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Instant getFecha() { return fecha; }
    public void setFecha(Instant fecha) { this.fecha = fecha; }
}
