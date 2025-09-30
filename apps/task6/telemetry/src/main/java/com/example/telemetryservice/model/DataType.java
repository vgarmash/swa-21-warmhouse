package com.example.telemetryservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Типы данных измерений")
public enum DataType {
    @Schema(description = "Температура")
    TEMPERATURE,

    @Schema(description = "Влажность")
    HUMIDITY,

    @Schema(description = "Давление")
    PRESSURE,

    @Schema(description = "Напряжение")
    VOLTAGE,

    @Schema(description = "Ток")
    CURRENT,

    @Schema(description = "Мощность")
    POWER,

    @Schema(description = "Энергия")
    ENERGY,

    @Schema(description = "Состояние")
    STATE,

    @Schema(description = "Пользовательский тип")
    CUSTOM
}