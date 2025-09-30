package com.example.telemetryservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Schema(description = "Телеметрические данные устройства")
public record TelemetryData(
        @NotNull
        @JsonProperty("source_id")
        @Schema(description = "ID источника данных", example = "123")
        Long sourceId,

        @NotNull
        @JsonProperty("source_type")
        @Schema(description = "Тип источника данных", example = "SENSOR")
        DeviceType sourceType,

        @NotNull
        @Schema(description = "Значение измерения", example = "25.5")
        String value,

        @NotNull
        @JsonProperty("data_type")
        @Schema(description = "Тип данных", example = "TEMPERATURE")
        DataType dataType,

        @NotNull
        @Schema(description = "Временная метка измерения", example = "2024-01-15T12:00:00Z")
        Instant timestamp
) {}