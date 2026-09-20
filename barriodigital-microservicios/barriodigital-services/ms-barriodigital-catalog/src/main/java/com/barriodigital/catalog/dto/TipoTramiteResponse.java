package com.barriodigital.catalog.dto;

import com.barriodigital.catalog.model.TipoTramite;

public class TipoTramiteResponse {
    private Long id;
    private String nombre;
    private String requisitos;
    private Integer cupoDiarioTotal;
    private Integer cupoDiarioDisponible;

    public static TipoTramiteResponse fromEntity(TipoTramite t) {
        TipoTramiteResponse r = new TipoTramiteResponse();
        r.id = t.getId();
        r.nombre = t.getNombre();
        r.requisitos = t.getRequisitos();
        r.cupoDiarioTotal = t.getCupoDiarioTotal();
        r.cupoDiarioDisponible = t.getCupoDiarioDisponible();
        return r;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRequisitos() { return requisitos; }
    public Integer getCupoDiarioTotal() { return cupoDiarioTotal; }
    public Integer getCupoDiarioDisponible() { return cupoDiarioDisponible; }
}
