# Telemetry Service

Микросервис для работы с телеметрическими данными через MQTT и REST API с использованием InfluxDB.

## API Endpoints
### Основные endpoints:
`POST /api/v1/telemetry/search` - Поиск телеметрических данных с фильтрами

`GET /api/v1/telemetry/source/{sourceId}` - Получение данных по source_id

### Документация:
`GET /swagger-ui.html` - Интерактивная документация Swagger UI

`GET /api-docs` - OpenAPI спецификация в JSON формате

`GET /actuator/health` - Health check приложения

## Быстрый старт

```bash
# Сборка и запуск
docker-compose build --no-cache
docker-compose up -d

# Остановка
docker-compose down
```

## Проверка работы

```bash

# Health check
curl http://localhost:8080/actuator/health

# Тестовое MQTT сообщение
mosquitto_pub -h localhost -t "telemetry/test" -m '{
  "source_id": 123,
  "source_type": "SENSOR", 
  "value": "25.5",
  "data_type": "TEMPERATURE",
  "timestamp": "2024-01-15T12:00:00Z"
}'

# Поиск телеметрических данных
curl -X POST http://localhost:8080/api/v1/telemetry/search \
  -H "Content-Type: application/json" \
  -d '{"sourceIds": [123], "limit": 10}'
```