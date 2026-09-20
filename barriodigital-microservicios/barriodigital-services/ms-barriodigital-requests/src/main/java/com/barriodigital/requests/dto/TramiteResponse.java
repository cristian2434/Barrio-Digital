package com.barriodigital.requests.dto;

import com.barriodigital.requests.model.EstadoTramite;
import com.barriodigital.requests.model.Tramite;

import java.time.LocalDateTime;

/** DTO de salida: nunca exponemos la entidad JPA directamente. */
public class TramiteResponse {
    private Long id;
    private Long tipoTramiteId;
    private String vecinoUsername;
    private String descripcion;
    private EstadoTramite estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String funcionarioAsignado;

    public static TramiteResponse fromEntity(Tramite t) {
        TramiteResponse r = new TramiteResponse();
        r.id = t.getId();
        r.tipoTramiteId = t.getTipoTramiteId();
        r.vecinoUsername = t.getVecinoUsername();
        r.descripcion = t.getDescripcion();
        r.estado = t.getEstado();
        r.fechaCreacion = t.getFechaCreacion();
        r.fechaActualizacion = t.getFechaActualizacion();
        r.funcionarioAsignado = t.getFuncionarioAsignado();
        return r;
    }

    public Long getId() { return id; }
    public Long getTipoTramiteId() { return tipoTramiteId; }
    public String getVecinoUsername() { return vecinoUsername; }
    public String getDescripcion() { return descripcion; }
    public EstadoTramite getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public String getFuncionarioAsignado() { return funcionarioAsignado; }
}
