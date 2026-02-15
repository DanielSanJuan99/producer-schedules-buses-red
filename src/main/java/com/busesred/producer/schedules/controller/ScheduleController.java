package com.busesred.producer.schedules.controller;

import com.busesred.producer.schedules.model.ScheduleMessage;
import com.busesred.producer.schedules.service.ScheduleProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/schedules")
@Slf4j
public class ScheduleController {

    private final ScheduleProducerService producerService;

    public ScheduleController(ScheduleProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendSchedule(@RequestBody ScheduleMessage scheduleMessage) {
        try {
            if (scheduleMessage.getTimestamp() == null) {
                scheduleMessage.setTimestamp(LocalDateTime.now());
            }
            
            log.info("Recibida solicitud para enviar horario/cambio de ruta: {}", scheduleMessage);
            producerService.sendSchedule(scheduleMessage);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Horario/cambio de ruta enviado a RabbitMQ correctamente");
            response.put("data", scheduleMessage);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al enviar horario/cambio de ruta: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error al enviar horario: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "producer-schedules-buses-red");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
