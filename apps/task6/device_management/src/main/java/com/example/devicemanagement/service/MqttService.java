package com.example.devicemanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class MqttService {

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    @Value("${mqtt.broker-url:tcp://localhost:1883}")
    private String brokerUrl;

    @Value("${mqtt.client-id:device-service}")
    private String clientId;

    @Value("${mqtt.topic:devices/events}")
    private String topic;

    private MqttClient mqttClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            String actualClientId = clientId + "-" + System.currentTimeMillis();
            mqttClient = new MqttClient(brokerUrl, actualClientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(60);

            mqttClient.connect(options);
            logger.info("✅ Successfully connected to MQTT broker at: {}", brokerUrl);

        } catch (Exception e) {
            logger.warn("⚠️ Failed to connect to MQTT broker: {}. MQTT features will be disabled.", e.getMessage());
        }
    }

    public void publishDeviceEvent(String eventType, Object payload) {
        if (!isConnected()) {
            logger.debug("MQTT client not connected, skipping message publication");
            return;
        }

        try {
            String message = objectMapper.writeValueAsString(payload);
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(0); // Use QoS 0 for better performance
            mqttMessage.setRetained(false);

            String fullTopic = topic + "/" + eventType;
            mqttClient.publish(fullTopic, mqttMessage);
            logger.info("📤 Published MQTT message to topic: {}", fullTopic);

        } catch (Exception e) {
            logger.error("❌ Failed to publish MQTT message: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
                logger.info("Disconnected from MQTT broker");
            } catch (Exception e) {
                logger.error("Error disconnecting from MQTT broker: {}", e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        return mqttClient != null && mqttClient.isConnected();
    }
}