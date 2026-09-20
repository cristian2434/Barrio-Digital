package com.barriodigital.requests.model;

/**
 * Máquina de estados del trámite (sección 3 del caso BarrioDigital).
 * No se puede pasar a EN_TERRENO sin haber pasado antes por ADMITIDO.
 */
public enum EstadoTramite {
    INGRESADO,
    ADMITIDO,
    EN_GESTION,
    EN_TERRENO,
    RESUELTO,
    RECHAZADO
}
