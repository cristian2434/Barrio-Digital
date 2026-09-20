package com.barriodigital.notify;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topología RabbitMQ según sección 8 del caso: 3 colas principales + 3 DLQ.
 * Exchanges: cmd.direct (direct), cmd.topic (topic), cmd.dead.dlx (direct, para DLQ).
 */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_DIRECT = "cmd.direct";
    public static final String EXCHANGE_TOPIC = "cmd.topic";
    public static final String EXCHANGE_DLX = "cmd.dead.dlx";

    public static final String QUEUE_EMAIL = "q.cmd.email";
    public static final String QUEUE_CREW = "q.cmd.crew";
    public static final String QUEUE_CERTIFICATE = "q.cmd.certificate";

    @Bean
    public DirectExchange cmdDirectExchange() { return new DirectExchange(EXCHANGE_DIRECT); }

    @Bean
    public TopicExchange cmdTopicExchange() { return new TopicExchange(EXCHANGE_TOPIC); }

    @Bean
    public DirectExchange cmdDeadLetterExchange() { return new DirectExchange(EXCHANGE_DLX); }

    private Queue queueWithDlq(String name) {
        return QueueBuilder.durable(name)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
                .withArgument("x-dead-letter-routing-key", name + ".dlq")
                .build();
    }

    @Bean
    public Queue emailQueue() { return queueWithDlq(QUEUE_EMAIL); }

    @Bean
    public Queue crewQueue() { return queueWithDlq(QUEUE_CREW); }

    @Bean
    public Queue certificateQueue() { return queueWithDlq(QUEUE_CERTIFICATE); }

    @Bean
    public Queue emailDlq() { return QueueBuilder.durable(QUEUE_EMAIL + ".dlq").build(); }

    @Bean
    public Queue crewDlq() { return QueueBuilder.durable(QUEUE_CREW + ".dlq").build(); }

    @Bean
    public Queue certificateDlq() { return QueueBuilder.durable(QUEUE_CERTIFICATE + ".dlq").build(); }

    @Bean
    public Binding emailBinding() { return BindingBuilder.bind(emailQueue()).to(cmdDirectExchange()).with("email.send"); }

    @Bean
    public Binding crewBinding() { return BindingBuilder.bind(crewQueue()).to(cmdDirectExchange()).with("crew.ticket"); }

    @Bean
    public Binding certificateBinding() { return BindingBuilder.bind(certificateQueue()).to(cmdDirectExchange()).with("certificate.gen"); }

    @Bean
    public Binding emailDlqBinding() { return BindingBuilder.bind(emailDlq()).to(cmdDeadLetterExchange()).with(QUEUE_EMAIL + ".dlq"); }

    @Bean
    public Binding crewDlqBinding() { return BindingBuilder.bind(crewDlq()).to(cmdDeadLetterExchange()).with(QUEUE_CREW + ".dlq"); }

    @Bean
    public Binding certificateDlqBinding() { return BindingBuilder.bind(certificateDlq()).to(cmdDeadLetterExchange()).with(QUEUE_CERTIFICATE + ".dlq"); }

    /**
     * Los productores (ej. ms-barriodigital-requests) están en otro JAR/paquete,
     * así que el header "__TypeId__" que agrega Jackson2Json en el emisor apunta a
     * una clase que no existe en este classpath. Con TypePrecedence.INFERRED,
     * el converter ignora ese header y usa directamente el tipo del parámetro
     * del método @RabbitListener (NotificacionEvent) para deserializar por nombre
     * de campo JSON, que es lo que realmente necesitamos aquí.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(DefaultJackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
