# Producer Schedules - Buses RED 🕐

Microservicio productor que recibe horarios y cambios de ruta de buses y los publica en RabbitMQ para su procesamiento asíncrono.

## Tecnologías

- Java 21 (Eclipse Temurin)
- Spring Boot 3.5.7
- Spring AMQP (RabbitMQ)
- Jackson (serialización JSON)
- Maven 3.9.9
- Docker (multi-stage build)

## Arquitectura

```
POST /schedules/send
        │
        ▼
┌──────────────────────┐
│  ScheduleController   │
│  (valida y responde)  │
└────────┬─────────────┘
         │
         ▼
┌──────────────────────────┐
│  ScheduleProducerService  │
│  (publica en RabbitMQ)    │
└────────┬─────────────────┘
         │
         ▼
┌───────────────────────────────┐
│  RabbitMQ                      │
│  Exchange: schedules.exchange  │
│  Queue:    schedules.queue     │
│  Routing:  schedules.routing.key│
└───────────────────────────────┘
```

## Estructura del Proyecto

```
producer-schedules-buses-red/
├── src/main/java/com/busesred/producer/schedules/
│   ├── ProducerSchedulesApplication.java
│   ├── config/
│   │   └── RabbitMQConfig.java
│   ├── controller/
│   │   └── ScheduleController.java
│   ├── model/
│   │   └── ScheduleMessage.java
│   └── service/
│       └── ScheduleProducerService.java
├── src/main/resources/
│   └── application.yml
├── Dockerfile
└── pom.xml
```

## Modelo de Datos (JSON)

Todos los campos usan español snake_case via `@JsonProperty`:

```json
{
  "id_bus": "BUS-003",
  "ruta": "507",
  "nombre_ruta": "Ruta 507 - Centro",
  "hora_salida": "08:30:00",
  "hora_llegada": "09:15:00",
  "tipo_cambio": "SCHEDULE_UPDATE",
  "descripcion": "Actualización de horario matutino",
  "marca_tiempo": "2026-02-15T10:30:00",
  "origen": "Estación Central",
  "destino": "Providencia"
}
```

| Campo JSON | Campo Java | Tipo |
|---|---|---|
| `id_bus` | busId | String |
| `ruta` | route | String |
| `nombre_ruta` | routeName | String |
| `hora_salida` | departureTime | LocalTime |
| `hora_llegada` | arrivalTime | LocalTime |
| `tipo_cambio` | changeType | String |
| `descripcion` | description | String |
| `marca_tiempo` | timestamp | LocalDateTime |
| `origen` | origin | String |
| `destino` | destination | String |

> `marca_tiempo` se genera automáticamente si no se envía en el payload.

### Valores válidos para `tipo_cambio`

- `ROUTE_CHANGE` — Cambio de ruta
- `SCHEDULE_UPDATE` — Actualización de horario
- `DELAY` — Retraso

## Variables de Entorno

| Variable | Descripción | Default |
|---|---|---|
| `RABBITMQ_HOST` | Host de RabbitMQ | `rabbitmq` |
| `RABBITMQ_PORT` | Puerto de RabbitMQ | `5672` |
| `RABBITMQ_USERNAME` | Usuario RabbitMQ | *(requerido)* |
| `RABBITMQ_PASSWORD` | Contraseña RabbitMQ | *(requerido)* |

## Configuración RabbitMQ

| Recurso | Nombre |
|---|---|
| Exchange | `schedules.exchange` (TopicExchange) |
| Queue | `schedules.queue` (durable) |
| Routing Key | `schedules.routing.key` |
| Converter | `Jackson2JsonMessageConverter` |

## Endpoints

### Enviar Horario / Cambio de Ruta
```http
POST /schedules/send
Content-Type: application/json

{
  "id_bus": "BUS-003",
  "ruta": "507",
  "nombre_ruta": "Ruta 507 - Centro",
  "hora_salida": "08:30:00",
  "hora_llegada": "09:15:00",
  "tipo_cambio": "SCHEDULE_UPDATE",
  "descripcion": "Actualización horario",
  "origen": "Estación Central",
  "destino": "Providencia"
}
```

**Respuesta (200 OK):**
```json
{
  "estado": "exitoso",
  "mensaje": "Horario/cambio de ruta enviado a RabbitMQ correctamente",
  "datos": { ... },
  "marca_tiempo": "2026-02-15T10:30:00"
}
```

### Health Check
```http
GET /schedules/health
```

## Ejecución Local

```bash
mvn clean package -DskipTests

RABBITMQ_USERNAME=admin RABBITMQ_PASSWORD=admin123 \
java -jar target/producer-schedules-buses-red-1.0.0.jar
```

## Docker

```bash
docker build --no-cache --platform linux/amd64 -t producer-schedules-buses-red:latest .

docker run -p 8082:8082 \
  -e RABBITMQ_HOST=rabbitmq \
  -e RABBITMQ_USERNAME=admin \
  -e RABBITMQ_PASSWORD=admin123 \
  producer-schedules-buses-red:latest
```

## Puerto

| Servicio | Puerto |
|---|---|
| Producer Schedules | `8082` |
