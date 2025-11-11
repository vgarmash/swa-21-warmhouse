package com.example.telemetryservice.controller;

import com.example.telemetryservice.model.ApiResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseModel<?>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest()
                .body(ApiResponseModel.error("Validation failed: " + errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseModel<?>> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(ApiResponseModel.error("Internal server error: " + ex.getMessage()));
    }
}