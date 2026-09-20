package com.barriodigital.catalog.exception;

public class TipoTramiteNoEncontradoException extends RuntimeException {
    public TipoTramiteNoEncontradoException(Long id) {
        super("No existe el tipo de trámite con id " + id);
    }
}
