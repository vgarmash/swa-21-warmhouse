package com.example.devicemanagement.controller;

import com.example.devicemanagement.service.MqttService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Health", description = "API Health Check Endpoints")
public class HealthController {

    @Autowired
    private ApplicationAvailability availability;

    @Autowired
    private MqttService mqttService;

    @GetMapping("/health")
    @Operation(
            summary = "Application health check",
            description = "Provides comprehensive health status of the application including database and MQTT connections"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Application is healthy",
            content = @Content(schema = @Schema(implementation = Map.class))
    )
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();

        boolean mqttConnected = mqttService.isConnected();
        health.put("status", "UP");
        health.put("liveness", availability.getLivenessState());
        health.put("readiness", availability.getReadinessState());
        health.put("mqtt", mqttConnected ? "CONNECTED" : "DISCONNECTED");
        health.put("mqttBroker", mqttService.isConnected() ? "AVAILABLE" : "UNAVAILABLE");
        health.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(health);
    }

    @GetMapping("/ready")
    @Operation(
            summary = "Application readiness check",
            description = "Indicates whether the application is ready to accept traffic"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Application readiness status",
            content = @Content(schema = @Schema(implementation = Map.class))
    )
    public ResponseEntity<Map<String, Object>> readiness() {
        Map<String, Object> readiness = new HashMap<>();
        boolean isReady = availability.getReadinessState() == ReadinessState.ACCEPTING_TRAFFIC;
        readiness.put("status", isReady ? "READY" : "NOT_READY");
        readiness.put("database", "CONNECTED");
        readiness.put("mqtt", mqttService.isConnected() ? "CONNECTED" : "DISCONNECTED");
        return ResponseEntity.ok(readiness);
    }
}