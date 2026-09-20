package com.barriodigital.requests.dto;

import com.barriodigital.requests.model.EstadoTramite;
import jakarta.validation.constraints.NotNull;

/** DTO de entrada: body de PUT /api/requests/{id}/status */
public class CambiarEstadoRequest {

    @NotNull
    private EstadoTramite status;

    public EstadoTramite getStatus() { return status; }
    public void setStatus(EstadoTramite status) { this.status = status; }
}
