package com.example.telemetryservice.config;

import com.influxdb.client.InfluxDBClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Configuration
public class HealthCheckConfig {

    @Bean
    public HealthIndicator influxDBHealthIndicator(InfluxDBClient influxDBClient) {
        return () -> {
            try {
                boolean isConnected = influxDBClient.ping();
                if (isConnected) {
                    return Health.up()
                            .withDetail("database", "InfluxDB")
                            .withDetail("status", "connected")
                            .build();
                } else {
                    return Health.down()
                            .withDetail("database", "InfluxDB")
                            .withDetail("status", "disconnected")
                            .build();
                }
            } catch (Exception e) {
                return Health.down()
                        .withDetail("database", "InfluxDB")
                        .withDetail("error", e.getMessage())
                        .build();
            }
        };
    }

    @Bean
    public HealthIndicator mqttHealthIndicator(MqttPahoClientFactory mqttClientFactory) {
        return () -> {
            try {
                MqttConnectOptions options = mqttClientFactory.getConnectionOptions();
                if (options.getServerURIs() != null && options.getServerURIs().length > 0) {
                    String brokerUrl = options.getServerURIs()[0];

                    // Проверяем, что URL брокера валидный
                    if (brokerUrl != null && !brokerUrl.trim().isEmpty()) {
                        return Health.up()
                                .withDetail("service", "MQTT Broker")
                                .withDetail("status", "configuration_ok")
                                .withDetail("broker", brokerUrl)
                                .build();
                    } else {
                        return Health.down()
                                .withDetail("service", "MQTT Broker")
                                .withDetail("error", "Broker URL is empty")
                                .build();
                    }
                } else {
                    return Health.down()
                            .withDetail("service", "MQTT Broker")
                            .withDetail("error", "No MQTT broker configured")
                            .build();
                }
            } catch (Exception e) {
                return Health.down()
                        .withDetail("service", "MQTT Broker")
                        .withDetail("error", e.getMessage())
                        .build();
            }
        };
    }
}