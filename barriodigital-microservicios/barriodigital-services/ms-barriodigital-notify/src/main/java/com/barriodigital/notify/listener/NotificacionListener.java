package com.barriodigital.notify.listener;

import com.barriodigital.notify.RabbitConfig;
import com.barriodigital.notify.dto.NotificacionEvent;
import com.barriodigital.notify.service.NotificacionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Este microservicio no expone endpoints públicos (ver caso, sección 5:
 * "no público, consumidor RabbitMQ"). Su única entrada son estas 3 colas.
 *
 * TODO producción: idempotencia por eventId (evitar reprocesar el mismo
 * mensaje si RabbitMQ lo reintenta), y métricas de tasa de DLQ.
 */
@Component
public class NotificacionListener {

    private final NotificacionService service;

    public NotificacionListener(NotificacionService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_EMAIL)
    public void onEmail(NotificacionEvent event) {
        service.enviarEmail(event);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_CREW)
    public void onCrewTicket(NotificacionEvent event) {
        service.enviarTicketCuadrilla(event);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_CERTIFICATE)
    public void onCertificate(NotificacionEvent event) {
        service.generarCertificado(event);
    }
}
