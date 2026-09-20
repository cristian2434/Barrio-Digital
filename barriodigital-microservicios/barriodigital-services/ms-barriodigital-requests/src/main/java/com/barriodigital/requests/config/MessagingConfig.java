package com.barriodigital.requests.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Config del lado productor (ms-barriodigital-requests):
 * - RabbitTemplate serializa a JSON (no a bytes Java) para que ms-barriodigital-notify,
 *   que corre en otra JVM/paquete, pueda deserializarlo igual.
 * - El exchange "cmd.direct" ya lo declara ms-barriodigital-notify con sus colas;
 *   lo volvemos a declarar aquí (idempotente) para poder publicar aunque notify
 *   todavía no haya arrancado.
 * - RestTemplate simple para la llamada síncrona a ms-barriodigital-catalog.
 */
@Configuration
public class MessagingConfig {

    public static final String EXCHANGE_DIRECT = "cmd.direct";

    @Bean
    public DirectExchange cmdDirectExchange() {
        return new DirectExchange(EXCHANGE_DIRECT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Se declara explícita con tipo <String, Object> (en vez de confiar en el
     * KafkaTemplate<Object,Object> que autoconfigura Spring Boot) para que el
     * tipo genérico calce exacto con lo que inyecta TramiteEventPublisher.
     * Reutiliza toda la config de application.yml (bootstrap-servers, JsonSerializer,
     * spring.json.add.type.headers=false) vía KafkaProperties.
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties(null);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
