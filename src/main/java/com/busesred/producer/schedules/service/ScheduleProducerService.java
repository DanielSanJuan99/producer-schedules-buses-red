package com.busesred.producer.schedules.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.busesred.producer.schedules.model.ScheduleMessage;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduleProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.schedules}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key.schedules}")
    private String routingKey;

    public ScheduleProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSchedule(ScheduleMessage scheduleMessage) {
        log.info("Enviando horario/cambio de ruta a RabbitMQ: {}", scheduleMessage);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, scheduleMessage);
        log.info("Horario/cambio de ruta enviado exitosamente");
    }
}
