package com.example.telemetryservice.config;

import com.example.telemetryservice.service.MqttService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageHandler implements MessageHandler {

    private final MqttService mqttService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MqttMessageHandler(MqttService mqttService, ObjectMapper objectMapper) {
        this.mqttService = mqttService;
        this.objectMapper = objectMapper;
    }

    @Override
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) throws MessagingException {
        try {
            String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
            byte[] payload = (byte[]) message.getPayload();

            if (topic != null && payload != null) {
                mqttService.handleMessage(topic, new String(payload));
            }
        } catch (Exception e) {
            throw new MessagingException("Error processing MQTT message", e);
        }
    }
}