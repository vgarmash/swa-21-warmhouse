package com.example.telemetryservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Стандартный ответ API")
public record ApiResponseModel<T>(
        @Schema(description = "Флаг успешности операции")
        boolean success,

        @Schema(description = "Сообщение об ошибке (если есть)")
        String message,

        @Schema(description = "Данные ответа")
        T data
) {
    public static <T> ApiResponseModel<T> success(T data) {
        return new ApiResponseModel<>(true, null, data);
    }

    public static <T> ApiResponseModel<T> error(String message) {
        return new ApiResponseModel<>(false, message, null);
    }
}