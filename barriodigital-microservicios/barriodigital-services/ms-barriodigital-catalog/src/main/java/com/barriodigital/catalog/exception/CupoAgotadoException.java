package com.barriodigital.catalog.exception;

public class CupoAgotadoException extends RuntimeException {
    public CupoAgotadoException(Long id) {
        super("No hay cupo disponible hoy para el tipo de trámite " + id);
    }
}
