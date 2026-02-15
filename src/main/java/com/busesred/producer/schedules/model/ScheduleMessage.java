package com.busesred.producer.schedules.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMessage implements Serializable {
    private String busId;
    private String route;
    private String routeName;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String changeType; // ROUTE_CHANGE, SCHEDULE_UPDATE, DELAY
    private String description;
    private LocalDateTime timestamp;
    private String origin;
    private String destination;
}
