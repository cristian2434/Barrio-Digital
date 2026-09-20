package com.barriodigital.requests.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tramites")
public class Tramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tipoTramiteId; // referencia lógica a ms-barriodigital-catalog

    @Column(nullable = false)
    private String vecinoUsername; // preferred_username del JWT del vecino

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTramite estado = EstadoTramite.INGRESADO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private LocalDateTime fechaActualizacion;

    private String funcionarioAsignado;

    public Tramite() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTipoTramiteId() { return tipoTramiteId; }
    public void setTipoTramiteId(Long tipoTramiteId) { this.tipoTramiteId = tipoTramiteId; }

    public String getVecinoUsername() { return vecinoUsername; }
    public void setVecinoUsername(String vecinoUsername) { this.vecinoUsername = vecinoUsername; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoTramite getEstado() { return estado; }
    public void setEstado(EstadoTramite estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getFuncionarioAsignado() { return funcionarioAsignado; }
    public void setFuncionarioAsignado(String funcionarioAsignado) { this.funcionarioAsignado = funcionarioAsignado; }
}
