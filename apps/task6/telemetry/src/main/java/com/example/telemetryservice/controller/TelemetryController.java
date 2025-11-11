package com.example.telemetryservice.controller;

import com.example.telemetryservice.model.ApiResponseModel;
import com.example.telemetryservice.model.TelemetryData;
import com.example.telemetryservice.model.TelemetrySearchRequest;
import com.example.telemetryservice.model.DataType;
import com.example.telemetryservice.service.InfluxDBService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/telemetry")
@Tag(name = "Telemetry API", description = "API для работы с телеметрическими данными")
public class TelemetryController {

    private final InfluxDBService influxDBService;

    public TelemetryController(InfluxDBService influxDBService) {
        this.influxDBService = influxDBService;
    }

    @PostMapping("/search")
    @Operation(
            summary = "Поиск телеметрических данных",
            description = "Поиск телеметрических данных с различными фильтрами"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный поиск",
                    content = @Content(schema = @Schema(implementation = ApiResponseModel.class))),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ApiResponseModel<List<TelemetryData>> searchTelemetry(
            @Valid @RequestBody TelemetrySearchRequest searchRequest) {
        try {
            List<TelemetryData> telemetryData = influxDBService.searchTelemetryData(searchRequest);
            return ApiResponseModel.success(telemetryData);
        } catch (Exception e) {
            return ApiResponseModel.error("Error searching telemetry data: " + e.getMessage());
        }
    }

    @GetMapping("/source/{sourceId}")
    @Operation(
            summary = "Получение телеметрических данных по source_id",
            description = "Получение телеметрических данных для конкретного источника"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное получение данных"),
            @ApiResponse(responseCode = "404", description = "Данные не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ApiResponseModel<List<TelemetryData>> getTelemetryBySource(
            @Parameter(description = "ID источника данных", example = "123")
            @PathVariable Long sourceId,

            @Parameter(description = "Тип данных для фильтрации", example = "TEMPERATURE")
            @RequestParam(required = false) String dataType,

            @Parameter(description = "Начальное время (ISO format)", example = "2024-01-15T00:00:00Z")
            @RequestParam(required = false) String startTime,

            @Parameter(description = "Конечное время (ISO format)", example = "2024-01-15T23:59:59Z")
            @RequestParam(required = false) String endTime,

            @Parameter(description = "Лимит результатов", example = "100")
            @RequestParam(defaultValue = "100") Integer limit) {
        try {
            TelemetrySearchRequest searchRequest = new TelemetrySearchRequest(
                    List.of(sourceId),
                    null,
                    dataType != null ? DataType.valueOf(dataType.toUpperCase()) : null,
                    startTime != null ? java.time.Instant.parse(startTime) : null,
                    endTime != null ? java.time.Instant.parse(endTime) : null,
                    null,
                    limit
            );

            List<TelemetryData> telemetryData = influxDBService.searchTelemetryData(searchRequest);
            return ApiResponseModel.success(telemetryData);
        } catch (Exception e) {
            return ApiResponseModel.error("Error retrieving telemetry data: " + e.getMessage());
        }
    }
}