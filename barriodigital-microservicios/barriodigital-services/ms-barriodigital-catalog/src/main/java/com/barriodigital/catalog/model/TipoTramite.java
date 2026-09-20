package com.barriodigital.catalog.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_tramite")
public class TipoTramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(length = 2000)
    private String requisitos;

    @Column(nullable = false)
    private Integer cupoDiarioTotal;

    @Column(nullable = false)
    private Integer cupoDiarioDisponible;

    public TipoTramite() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    public Integer getCupoDiarioTotal() { return cupoDiarioTotal; }
    public void setCupoDiarioTotal(Integer cupoDiarioTotal) { this.cupoDiarioTotal = cupoDiarioTotal; }

    public Integer getCupoDiarioDisponible() { return cupoDiarioDisponible; }
    public void setCupoDiarioDisponible(Integer cupoDiarioDisponible) { this.cupoDiarioDisponible = cupoDiarioDisponible; }
}
