package com.barriodigital.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** DTO de entrada para crear/editar un tipo de trámite. */
public class TipoTramiteRequest {

    @NotBlank
    private String nombre;

    private String requisitos;

    @NotNull
    @Min(0)
    private Integer cupoDiarioTotal;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    public Integer getCupoDiarioTotal() { return cupoDiarioTotal; }
    public void setCupoDiarioTotal(Integer cupoDiarioTotal) { this.cupoDiarioTotal = cupoDiarioTotal; }
}
