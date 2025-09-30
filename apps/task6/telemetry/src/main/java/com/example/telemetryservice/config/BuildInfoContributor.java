package com.example.telemetryservice.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class BuildInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> buildDetails = new HashMap<>();
        buildDetails.put("name", "Telemetry Service");
        buildDetails.put("version", "1.0.0");
        buildDetails.put("description", "Микросервис для работы с телеметрическими данными");
        buildDetails.put("java.version", "21");
        buildDetails.put("spring-boot.version", "3.2.0");
        buildDetails.put("build.time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        builder.withDetail("build", buildDetails);

        Map<String, Object> serviceDetails = new HashMap<>();
        serviceDetails.put("status", "operational");
        serviceDetails.put("features", new String[]{
                "MQTT Telemetry Data Ingestion",
                "InfluxDB Storage",
                "REST API for Data Querying",
                "Health Monitoring",
                "Swagger Documentation"
        });

        builder.withDetail("service", serviceDetails);
    }
}