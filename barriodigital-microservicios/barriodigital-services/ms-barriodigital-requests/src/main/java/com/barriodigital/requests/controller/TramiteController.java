package com.barriodigital.requests.controller;

import com.barriodigital.requests.dto.CambiarEstadoRequest;
import com.barriodigital.requests.dto.CrearTramiteRequest;
import com.barriodigital.requests.dto.TramiteResponse;
import com.barriodigital.requests.model.EstadoTramite;
import com.barriodigital.requests.service.TramiteService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Endpoints según sección "Endpoints esenciales" del caso BarrioDigital.
 * La autorización por rol ya la validó el BFF antes de reenviar aquí;
 * este microservicio recibe el usuario autenticado vía el header X-User
 * que agrega el BFF (ver nota en README).
 */
@RestController
@RequestMapping("/api/requests")
public class TramiteController {

    private final TramiteService service;

    public TramiteController(TramiteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TramiteResponse crear(@Valid @RequestBody CrearTramiteRequest request,
                                  @RequestHeader(value = "X-User", required = false, defaultValue = "anonimo") String vecino) {
        return service.crear(request, vecino);
    }

    @GetMapping("/{id}")
    public TramiteResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<TramiteResponse> listar(
            @RequestParam(required = false) EstadoTramite status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.listar(status, from, to);
    }

    @PutMapping("/{id}/status")
    public TramiteResponse cambiarEstado(@PathVariable Long id,
                                          @Valid @RequestBody CambiarEstadoRequest request,
                                          @RequestHeader(value = "X-User", required = false) String funcionario) {
        return service.cambiarEstado(id, request.getStatus(), funcionario);
    }
}
