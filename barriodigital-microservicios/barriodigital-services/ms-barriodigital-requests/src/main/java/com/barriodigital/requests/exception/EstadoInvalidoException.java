package com.barriodigital.requests.exception;

/** Se lanza cuando se intenta una transición de estado no permitida por la regla de negocio. */
public class EstadoInvalidoException extends RuntimeException {
    public EstadoInvalidoException(String message) {
        super(message);
    }
}
