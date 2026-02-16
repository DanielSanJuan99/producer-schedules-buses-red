package com.busesred.producer.schedules.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMessage implements Serializable {

    @JsonProperty("id_bus")
    private String busId;

    @JsonProperty("ruta")
    private String route;

    @JsonProperty("nombre_ruta")
    private String routeName;

    @JsonProperty("hora_salida")
    private LocalTime departureTime;

    @JsonProperty("hora_llegada")
    private LocalTime arrivalTime;

    @JsonProperty("tipo_cambio")
    private String changeType; // ROUTE_CHANGE, SCHEDULE_UPDATE, DELAY

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("marca_tiempo")
    private LocalDateTime timestamp;

    @JsonProperty("origen")
    private String origin;

    @JsonProperty("destino")
    private String destination;
}
