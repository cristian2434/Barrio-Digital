package com.barriodigital.catalog.controller;

import com.barriodigital.catalog.dto.TipoTramiteRequest;
import com.barriodigital.catalog.dto.TipoTramiteResponse;
import com.barriodigital.catalog.service.TipoTramiteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints según sección "ms-barriodigital-catalog" del caso BarrioDigital. */
@RestController
@RequestMapping("/api/catalog/procedures")
public class TipoTramiteController {

    private final TipoTramiteService service;

    public TipoTramiteController(TipoTramiteService service) {
        this.service = service;
    }

    @GetMapping
    public List<TipoTramiteResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public TipoTramiteResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoTramiteResponse crear(@Valid @RequestBody TipoTramiteRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public TipoTramiteResponse actualizar(@PathVariable Long id, @Valid @RequestBody TipoTramiteRequest request) {
        return service.actualizar(id, request);
    }

    /** Llamado internamente por ms-barriodigital-requests al admitir un trámite. */
    @PutMapping("/{id}/descontar-cupo")
    public TipoTramiteResponse descontarCupo(@PathVariable Long id) {
        return service.descontarCupo(id);
    }
}
