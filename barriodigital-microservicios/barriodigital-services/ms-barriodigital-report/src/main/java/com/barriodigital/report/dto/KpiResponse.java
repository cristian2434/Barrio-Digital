package com.barriodigital.report.dto;

import java.util.Map;

/** GET /api/report/kpis */
public class KpiResponse {
    private long tramitesPorHora;
    private double tiempoResolucionPromedioHoras;
    private Map<String, Long> estadosActivos;

    public KpiResponse(long tramitesPorHora, double tiempoResolucionPromedioHoras, Map<String, Long> estadosActivos) {
        this.tramitesPorHora = tramitesPorHora;
        this.tiempoResolucionPromedioHoras = tiempoResolucionPromedioHoras;
        this.estadosActivos = estadosActivos;
    }

    public long getTramitesPorHora() { return tramitesPorHora; }
    public double getTiempoResolucionPromedioHoras() { return tiempoResolucionPromedioHoras; }
    public Map<String, Long> getEstadosActivos() { return estadosActivos; }
}
