#!/bin/bash

echo "🚀 Starting Smart Home System..."

# Создаем общую сеть если не существует
docker network create smart-home-network 2>/dev/null || true

# Останавливаем и удаляем предыдущие контейнеры
echo "🧹 Cleaning up previous containers..."
docker-compose down

# Запускаем все сервисы
echo "🔧 Starting all services..."
docker-compose up -d

# Ждем запуска сервисов
echo "⏳ Waiting for services to start..."
sleep 30

# Проверяем статус сервисов
echo "🔍 Checking services status..."

check_service() {
    local name=$1
    local url=$2
    if curl -s --head --request GET $url | grep "200\|404\|401" > /dev/null; then
        echo "✅ $name is running at $url"
    else
        echo "❌ $name is not responding at $url"
    fi
}

echo ""
echo "📊 Services Status:"
check_service "Device Management API" "http://localhost:8080/health"
check_service "Smart Home API" "http://localhost:8081/health"
check_service "Telemetry Service" "http://localhost:8082/actuator/health"
check_service "Grafana" "http://localhost:3000"
check_service "InfluxDB" "http://localhost:8086"

echo ""
echo "🎯 API Endpoints:"
echo "   Device Management: http://localhost:8080"
echo "   Smart Home:        http://localhost:8081"
echo "   Telemetry:         http://localhost:8082"
echo "   Grafana:           http://localhost:3000 (admin/admin)"
echo "   InfluxDB:          http://localhost:8086"
echo ""
echo "📡 MQTT Broker: localhost:1883"
echo ""
echo "✅ All services are deployed!"
echo "💡 Use 'docker-compose logs -f' to see logs"
echo "💡 Use 'docker-compose down' to stop all services"