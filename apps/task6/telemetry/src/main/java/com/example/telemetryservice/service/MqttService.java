package com.example.telemetryservice.service;

import com.example.telemetryservice.model.TelemetryData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class MqttService {

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    private final MqttPahoClientFactory clientFactory;
    private final InfluxDBService influxDBService;
    private final ObjectMapper objectMapper;

    @Value("${mqtt.broker-url}")
    private String brokerUrl;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String topic;

    @Value("${mqtt.qos}")
    private int qos;

    public MqttService(MqttPahoClientFactory clientFactory,
                       InfluxDBService influxDBService,
                       ObjectMapper objectMapper) {
        this.clientFactory = clientFactory;
        this.influxDBService = influxDBService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        startMqttListener();
    }

    private void startMqttListener() {
        try {
            MqttPahoMessageDrivenChannelAdapter adapter =
                    new MqttPahoMessageDrivenChannelAdapter(brokerUrl, clientId, clientFactory, topic);

            adapter.setCompletionTimeout(5000);
            adapter.setQos(qos);

            logger.info("MQTT listener started for topic: {}", topic);

        } catch (Exception e) {
            logger.error("Failed to start MQTT listener", e);
        }
    }

    public void handleMessage(String topic, String payload) {
        try {
            logger.debug("Received MQTT message from topic {}: {}", topic, payload);

            TelemetryData telemetryData = objectMapper.readValue(payload, TelemetryData.class);
            influxDBService.saveTelemetryData(telemetryData);

            logger.info("Successfully processed telemetry data from source: {}", telemetryData.sourceId());

        } catch (Exception e) {
            logger.error("Error processing MQTT message", e);
        }
    }
}