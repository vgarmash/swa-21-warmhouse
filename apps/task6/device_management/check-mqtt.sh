#!/bin/bash

echo "Testing MQTT connection..."

# Проверяем, доступен ли MQTT порт
if nc -z localhost 1883; then
    echo "✅ MQTT port 1883 is open"
else
    echo "❌ MQTT port 1883 is not accessible"
    exit 1
fi

# Пробуем подключиться с помощью mosquitto_pub (если установлен)
if command -v mosquitto_pub &> /dev/null; then
    echo "Testing MQTT publish..."
    if mosquitto_pub -h localhost -p 1883 -t "test/connection" -m "hello" -q 0; then
        echo "✅ MQTT publish test successful"
    else
        echo "❌ MQTT publish test failed"
    fi
else
    echo "ℹ️ mosquitto_pub not installed, skipping publish test"
fi

echo "MQTT test completed"