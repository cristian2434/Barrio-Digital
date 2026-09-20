package com.barriodigital.catalog.service;

import com.barriodigital.catalog.dto.TipoTramiteRequest;
import com.barriodigital.catalog.dto.TipoTramiteResponse;
import com.barriodigital.catalog.exception.CupoAgotadoException;
import com.barriodigital.catalog.exception.TipoTramiteNoEncontradoException;
import com.barriodigital.catalog.model.TipoTramite;
import com.barriodigital.catalog.repository.TipoTramiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoTramiteService {

    private final TipoTramiteRepository repository;

    public TipoTramiteService(TipoTramiteRepository repository) {
        this.repository = repository;
    }

    public TipoTramiteResponse crear(TipoTramiteRequest request) {
        TipoTramite tipo = new TipoTramite();
        tipo.setNombre(request.getNombre());
        tipo.setRequisitos(request.getRequisitos());
        tipo.setCupoDiarioTotal(request.getCupoDiarioTotal());
        tipo.setCupoDiarioDisponible(request.getCupoDiarioTotal());
        return TipoTramiteResponse.fromEntity(repository.save(tipo));
    }

    public List<TipoTramiteResponse> listar() {
        return repository.findAll().stream().map(TipoTramiteResponse::fromEntity).toList();
    }

    public TipoTramiteResponse buscarPorId(Long id) {
        return TipoTramiteResponse.fromEntity(obtenerOFallar(id));
    }

    public TipoTramiteResponse actualizar(Long id, TipoTramiteRequest request) {
        TipoTramite tipo = obtenerOFallar(id);
        tipo.setNombre(request.getNombre());
        tipo.setRequisitos(request.getRequisitos());
        tipo.setCupoDiarioTotal(request.getCupoDiarioTotal());
        // Nota: al editar el cupo total no se reduce el disponible ya consumido hoy.
        return TipoTramiteResponse.fromEntity(repository.save(tipo));
    }

    /**
     * "El cupo diario disminuye al admitir el trámite" (sección 3 del caso).
     * Este método lo llamaría ms-barriodigital-requests (vía el BFF) al pasar
     * un trámite de INGRESADO -> ADMITIDO.
     */
    public TipoTramiteResponse descontarCupo(Long id) {
        TipoTramite tipo = obtenerOFallar(id);
        if (tipo.getCupoDiarioDisponible() <= 0) {
            throw new CupoAgotadoException(id);
        }
        tipo.setCupoDiarioDisponible(tipo.getCupoDiarioDisponible() - 1);
        return TipoTramiteResponse.fromEntity(repository.save(tipo));
    }

    private TipoTramite obtenerOFallar(Long id) {
        return repository.findById(id).orElseThrow(() -> new TipoTramiteNoEncontradoException(id));
    }
}
