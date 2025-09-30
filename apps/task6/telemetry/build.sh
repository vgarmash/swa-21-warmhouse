#!/bin/bash

# Скрипт сборки Telemetry Service внутри Docker

set -e

echo "Building Telemetry Service inside Docker..."

# Создаем Docker образ для сборки
docker build -t telemetry-service-builder -f Dockerfile.build .

# Запускаем контейнер для сборки и копируем JAR файл
docker run --name telemetry-builder-container telemetry-service-builder

# Копируем собранный JAR из контейнера
docker cp telemetry-builder-container:/app/target/telemetry-service-1.0.0.jar ./target/

# Удаляем временный контейнер
docker rm telemetry-builder-container

# Собираем основной образ приложения
docker-compose build telemetry-service

echo "Build completed successfully!"
echo "You can now start the service with: ./start.sh"