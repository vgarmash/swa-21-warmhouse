#!/bin/bash

echo "Stopping any running containers..."
docker-compose down

echo "Building Docker images..."
docker-compose build --no-cache

echo "Starting services..."
docker-compose up -d

echo "Waiting for services to start..."
sleep 10

echo "Checking MQTT service..."
if docker ps | grep -q device-mqtt; then
    echo "✅ MQTT service is running"
else
    echo "❌ MQTT service failed to start"
    docker-compose logs mqtt
    exit 1
fi

echo "Checking PostgreSQL service..."
if docker ps | grep -q device-postgres; then
    echo "✅ PostgreSQL service is running"
else
    echo "❌ PostgreSQL service failed to start"
    docker-compose logs postgres
    exit 1
fi

echo "Waiting for Device Service to start..."
for i in {1..30}; do
    if curl -s http://localhost:8080/health > /dev/null 2>&1; then
        echo "✅ Device Service is up and running!"
        echo ""
        echo "=== Service URLs ==="
        echo "API: http://localhost:8080/devices"
        echo "Health: http://localhost:8080/health"
        echo "Swagger UI: http://localhost:8080/swagger-ui.html"
        echo ""
        echo "=== Check MQTT Connection ==="
        curl -s http://localhost:8080/health | grep mqtt
        echo ""
        echo "Use 'docker-compose logs -f device-service' to see application logs"
        echo "Use 'docker-compose logs -f mqtt' to see MQTT logs"
        break
    else
        if [ $i -eq 30 ]; then
            echo "❌ Service failed to start within 5 minutes."
            docker-compose logs device-service
            exit 1
        fi
        echo "⏳ Waiting for device service... ($i/30)"
        sleep 10
    fi
done