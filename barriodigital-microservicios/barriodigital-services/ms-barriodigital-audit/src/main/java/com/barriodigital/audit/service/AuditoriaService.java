package com.barriodigital.audit.service;

import com.barriodigital.audit.dto.EventoAuditoriaResponse;
import com.barriodigital.audit.model.EventoAuditoria;
import com.barriodigital.audit.repository.EventoAuditoriaRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuditoriaService {

    private final EventoAuditoriaRepository repository;

    public AuditoriaService(EventoAuditoriaRepository repository) {
        this.repository = repository;
    }

    /** Invocado por el listener de Kafka al llegar un evento nuevo. */
    public void registrar(Long tramiteId, String usuario, String accion, String detalle) {
        EventoAuditoria evento = new EventoAuditoria();
        evento.setTramiteId(tramiteId);
        evento.setUsuario(usuario);
        evento.setAccion(accion);
        evento.setDetalle(detalle);
        evento.setFecha(Instant.now());
        repository.save(evento);
    }

    public List<EventoAuditoriaResponse> listarTodo() {
        return repository.findAll().stream().map(EventoAuditoriaResponse::fromEntity).toList();
    }

    public List<EventoAuditoriaResponse> porTramite(Long tramiteId) {
        return repository.findByTramiteId(tramiteId).stream().map(EventoAuditoriaResponse::fromEntity).toList();
    }

    public List<EventoAuditoriaResponse> porUsuario(String usuario) {
        return repository.findByUsuario(usuario).stream().map(EventoAuditoriaResponse::fromEntity).toList();
    }
}
