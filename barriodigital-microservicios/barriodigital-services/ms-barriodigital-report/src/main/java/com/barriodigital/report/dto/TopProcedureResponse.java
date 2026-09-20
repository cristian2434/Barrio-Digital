package com.barriodigital.report.dto;

/** Item de GET /api/report/top-procedures */
public class TopProcedureResponse {
    private Long tipoTramiteId;
    private long cantidad;

    public TopProcedureResponse(Long tipoTramiteId, long cantidad) {
        this.tipoTramiteId = tipoTramiteId;
        this.cantidad = cantidad;
    }

    public Long getTipoTramiteId() { return tipoTramiteId; }
    public long getCantidad() { return cantidad; }
}
