package com.barriodigital.report.repository;

import com.barriodigital.report.model.EventoTramite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EventoTramiteRepository extends JpaRepository<EventoTramite, Long> {
    List<EventoTramite> findByFechaAfter(Instant desde);
    List<EventoTramite> findByEstado(String estado);
}
