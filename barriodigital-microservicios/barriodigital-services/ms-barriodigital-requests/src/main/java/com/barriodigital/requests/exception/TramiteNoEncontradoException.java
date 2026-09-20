package com.barriodigital.requests.exception;

public class TramiteNoEncontradoException extends RuntimeException {
    public TramiteNoEncontradoException(Long id) {
        super("No existe el trámite con id " + id);
    }
}
