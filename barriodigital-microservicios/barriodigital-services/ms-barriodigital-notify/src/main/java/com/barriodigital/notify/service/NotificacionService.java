package com.barriodigital.notify.service;

import com.barriodigital.notify.dto.NotificacionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Lógica de "envío" real (email/push/PDF). Por ahora solo loguea;
 * aquí se integraría un proveedor real (SendGrid, SES, Firebase, etc.).
 */
@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    public void enviarEmail(NotificacionEvent event) {
        log.info("[EMAIL] Trámite {} -> {}: {}", event.getTramiteId(), event.getDestinatario(), event.getMensaje());
    }

    public void enviarTicketCuadrilla(NotificacionEvent event) {
        log.info("[TICKET CUADRILLA] Trámite {} asignado a terreno: {}", event.getTramiteId(), event.getMensaje());
    }

    public void generarCertificado(NotificacionEvent event) {
        log.info("[CERTIFICADO] Generando PDF para trámite {}", event.getTramiteId());
    }
}
