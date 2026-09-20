package com.barriodigital.audit.controller;

import com.barriodigital.audit.dto.EventoAuditoriaResponse;
import com.barriodigital.audit.service.AuditoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de solo lectura para el rol Auditor.
 * La autorización (hasAnyRole Admin, Auditor) ya la aplica el BFF.
 */
@RestController
@RequestMapping("/api/audit")
public class AuditoriaController {

    private final AuditoriaService service;

    public AuditoriaController(AuditoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventoAuditoriaResponse> listar(
            @RequestParam(required = false) Long tramiteId,
            @RequestParam(required = false) String usuario) {
        if (tramiteId != null) return service.porTramite(tramiteId);
        if (usuario != null) return service.porUsuario(usuario);
        return service.listarTodo();
    }
}
