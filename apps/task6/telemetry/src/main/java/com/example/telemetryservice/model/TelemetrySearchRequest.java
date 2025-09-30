package com.example.telemetryservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Запрос для поиска телеметрических данных")
public record TelemetrySearchRequest(
        @Schema(description = "Список ID источников для фильтрации")
        List<Long> sourceIds,

        @Schema(description = "Тип источника для фильтрации")
        DeviceType sourceType,

        @Schema(description = "Тип данных для фильтрации")
        DataType dataType,

        @Schema(description = "Начальное время для фильтрации", example = "2024-01-15T00:00:00Z")
        Instant startTime,

        @Schema(description = "Конечное время для фильтрации", example = "2024-01-15T23:59:59Z")
        Instant endTime,

        @Schema(description = "Подстрока для поиска в значениях")
        String valueContains,

        @Schema(description = "Лимит результатов", example = "100")
        Integer limit
) {}