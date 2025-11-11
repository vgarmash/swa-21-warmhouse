package com.example.devicemanagement.service;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@ConditionalOnProperty(name = "mqtt.test-subscriber", havingValue = "true")
public class MqttTestSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(MqttTestSubscriber.class);

    @Value("${mqtt.broker-url:tcp://localhost:1883}")
    private String brokerUrl;

    @Value("${mqtt.topic:devices/events}")
    private String topic;

    private MqttClient mqttClient;

    @PostConstruct
    public void init() {
        try {
            String clientId = "test-subscriber-" + System.currentTimeMillis();
            mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    logger.info("Test subscriber connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String payload = new String(message.getPayload());
                    logger.info("📨 MQTT Message received - Topic: {}, Payload: {}", topic, payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used for subscriber
                }
            });

            mqttClient.connect(options);
            mqttClient.subscribe(topic + "/#");
            logger.info("Test subscriber connected and listening to: {}", topic + "/#");

        } catch (MqttException e) {
            logger.warn("Test subscriber failed to connect: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
                logger.info("Test subscriber disconnected");
            } catch (MqttException e) {
                logger.error("Error disconnecting test subscriber: {}", e.getMessage());
            }
        }
    }
}