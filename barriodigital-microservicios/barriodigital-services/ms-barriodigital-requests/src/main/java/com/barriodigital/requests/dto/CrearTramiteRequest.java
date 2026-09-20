package com.barriodigital.requests.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTO de entrada: lo que manda el vecino/funcionario al crear un trámite. */
public class CrearTramiteRequest {

    @NotNull
    private Long tipoTramiteId;

    @Size(max = 1000)
    private String descripcion;

    public Long getTipoTramiteId() { return tipoTramiteId; }
    public void setTipoTramiteId(Long tipoTramiteId) { this.tipoTramiteId = tipoTramiteId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
