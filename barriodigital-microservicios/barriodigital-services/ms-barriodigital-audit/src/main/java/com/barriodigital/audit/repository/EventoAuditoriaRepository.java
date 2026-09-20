package com.barriodigital.audit.repository;

import com.barriodigital.audit.model.EventoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoAuditoriaRepository extends JpaRepository<EventoAuditoria, Long> {
    List<EventoAuditoria> findByTramiteId(Long tramiteId);
    List<EventoAuditoria> findByUsuario(String usuario);
}
