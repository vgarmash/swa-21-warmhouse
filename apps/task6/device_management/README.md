# Инструкции по использованию:
## Способ 1: С использованием Docker Compose (рекомендуется)


# Даем права на выполнение скрипта
```bash

chmod +x build-and-run.sh

# Запускаем сборку и запуск
`./build-and-run.sh`

# Или вручную
docker-compose up --build
```
## Способ 2: Ручная сборка и запуск
```bash

# Сборка образа
docker build -t device-management-service .

# Запуск с зависимостями
docker-compose up postgres mqtt -d

# Запуск сервиса
docker run -p 8082:8080 \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/device_management \
-e MQTT_BROKER_URL=tcp://host.docker.internal:1883 \
device-management-service
```

## Проверка работы:
```bash
# Проверка здоровья
curl http://localhost:8082/health

# Проверка Swagger документации
open http://localhost:8082/swagger-ui.html

# Просмотр логов
docker-compose logs -f device-service
```