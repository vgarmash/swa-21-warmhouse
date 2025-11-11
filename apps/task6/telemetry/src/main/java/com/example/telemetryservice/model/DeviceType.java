package com.example.telemetryservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Типы устройств")
public enum DeviceType {
    @Schema(description = "Сенсор")
    SENSOR,

    @Schema(description = "Устройство")
    DEVICE,

    @Schema(description = "Хаб")
    HUB
}