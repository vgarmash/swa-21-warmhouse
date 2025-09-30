package com.example.devicemanagement.controller;

import com.example.devicemanagement.dto.DeviceCreateRequest;
import com.example.devicemanagement.model.Device;
import com.example.devicemanagement.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
@Tag(name = "Devices", description = "IoT Devices Management API")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    @Operation(
            summary = "Register a new device",
            description = "Registers a new IoT device in the system. Required fields: deviceKey and typeCode."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Device successfully registered",
                    content = @Content(schema = @Schema(implementation = Device.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or device key already exists",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    public ResponseEntity<?> registerDevice(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Device registration data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DeviceCreateRequest.class))
            )
            @RequestBody DeviceCreateRequest request) {
        try {
            Device device = deviceService.registerDevice(request);
            return ResponseEntity.ok(device);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error registering device");
        }
    }

    @GetMapping
    @Operation(
            summary = "List all devices",
            description = "Retrieves a list of devices. Can be filtered by homeId, typeCode, or location."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved devices list",
                    content = @Content(schema = @Schema(implementation = Device[].class))
            )
    })
    public ResponseEntity<List<Device>> listDevices(
            @Parameter(
                    description = "Filter by home ID",
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @RequestParam(required = false) UUID homeId,

            @Parameter(
                    description = "Filter by device type code",
                    example = "THERMOSTAT"
            )
            @RequestParam(required = false) String typeCode,

            @Parameter(
                    description = "Filter by device location",
                    example = "Living Room"
            )
            @RequestParam(required = false) String location) {

        List<Device> devices = deviceService.listDevices(homeId, typeCode, location);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get device by ID",
            description = "Retrieves a specific device by its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Device found",
                    content = @Content(schema = @Schema(implementation = Device.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Device not found"
            )
    })
    public ResponseEntity<Device> getDeviceById(
            @Parameter(
                    description = "Device UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        return deviceService.getDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}