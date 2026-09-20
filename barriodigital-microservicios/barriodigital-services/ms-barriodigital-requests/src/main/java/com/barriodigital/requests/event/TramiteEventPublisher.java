package com.barriodigital.requests.event;

import com.barriodigital.requests.config.MessagingConfig;
import com.barriodigital.requests.model.EstadoTramite;
import com.barriodigital.requests.model.Tramite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce los cambios de estado de un trámite (sección 3 del caso) en los
 * eventos async que el resto de la arquitectura espera (sección 8-9):
 * - Kafka "requests.events" -> lo consumen ms-barriodigital-report y, para
 *   auditoría, también puede derivar en un evento de "audit.timeline".
 * - RabbitMQ "cmd.direct" -> colas q.cmd.certificate / q.cmd.crew / q.cmd.email
 *   que consume ms-barriodigital-notify.
 */
@Component
public class TramiteEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(TramiteEventPublisher.class);

    private static final String TOPIC_REQUESTS_EVENTS = "requests.events";
    private static final String ROUTING_KEY_EMAIL = "email.send";
    private static final String ROUTING_KEY_CREW = "crew.ticket";
    private static final String ROUTING_KEY_CERTIFICATE = "certificate.gen";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RabbitTemplate rabbitTemplate;

    public TramiteEventPublisher(KafkaTemplate<String, Object> kafkaTemplate, RabbitTemplate rabbitTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Se llama en cada cambio de estado (incluida la creación -> INGRESADO).
     * Nombres de campo alineados 1:1 con RequestsEventsKafkaListener (ms-barriodigital-report):
     * tramiteId, tipoTramiteId, estado. Se agregan estadoAnterior/usuario/timestamp
     * como contexto extra; report los ignora si no los necesita.
     */
    public void publicarCambioEstado(Tramite tramite, EstadoTramite estadoAnterior, String actor) {
        Map<String, Object> evento = new LinkedHashMap<>();
        evento.put("tramiteId", tramite.getId());
        evento.put("tipoTramiteId", tramite.getTipoTramiteId());
        evento.put("estado", tramite.getEstado().name());
        evento.put("estadoAnterior", estadoAnterior == null ? null : estadoAnterior.name());
        evento.put("usuario", actor);
        evento.put("timestamp", Instant.now().toString());

        try {
            kafkaTemplate.send(TOPIC_REQUESTS_EVENTS, tramite.getId().toString(), evento);
            log.info("Evento publicado en '{}' para trámite {}: {} -> {}",
                    TOPIC_REQUESTS_EVENTS, tramite.getId(), estadoAnterior, tramite.getEstado());
        } catch (Exception ex) {
            // No debe tumbar la transacción del trámite si Kafka está caído en un dev/demo local.
            log.error("No se pudo publicar el evento en Kafka para el trámite {}: {}", tramite.getId(), ex.getMessage());
        }
    }

    /** INGRESADO: comprobante de ingreso (certificado/PDF) para el vecino. */
    public void publicarComprobanteIngreso(Tramite tramite) {
        NotificacionEvent event = new NotificacionEvent(
                "COMPROBANTE_INGRESO", tramite.getId(), tramite.getVecinoUsername(),
                "Se generó el comprobante de ingreso de tu trámite #" + tramite.getId());
        publicarRabbit(ROUTING_KEY_CERTIFICATE, event);
    }

    /** EN_TERRENO: se genera el ticket para la cuadrilla. */
    public void publicarTicketCuadrilla(Tramite tramite) {
        NotificacionEvent event = new NotificacionEvent(
                "TICKET_CUADRILLA", tramite.getId(), tramite.getFuncionarioAsignado(),
                "Trámite #" + tramite.getId() + " listo para visita en terreno");
        publicarRabbit(ROUTING_KEY_CREW, event);
    }

    /** RESUELTO: se notifica por email al vecino. */
    public void publicarNotificacionResuelto(Tramite tramite) {
        NotificacionEvent event = new NotificacionEvent(
                "TRAMITE_RESUELTO", tramite.getId(), tramite.getVecinoUsername(),
                "Tu trámite #" + tramite.getId() + " fue resuelto");
        publicarRabbit(ROUTING_KEY_EMAIL, event);
    }

    private void publicarRabbit(String routingKey, NotificacionEvent event) {
        try {
            rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE_DIRECT, routingKey, event);
            log.info("Mensaje publicado en exchange '{}' con routing key '{}' para trámite {}",
                    MessagingConfig.EXCHANGE_DIRECT, routingKey, event.getTramiteId());
        } catch (Exception ex) {
            log.error("No se pudo publicar en RabbitMQ (routing key {}) para el trámite {}: {}",
                    routingKey, event.getTramiteId(), ex.getMessage());
        }
    }
}
