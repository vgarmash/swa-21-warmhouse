package com.example.devicemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Request object for creating a new device")
public class DeviceCreateRequest {

    @NotNull(message = "deviceKey is required")
    @Schema(
            description = "Unique identifier key for the device",
            example = "device-12345",
            required = true
    )
    private String deviceKey;

    @NotNull(message = "typeCode is required")
    @Schema(
            description = "Type code of the device",
            example = "THERMOSTAT",
            required = true
    )
    private String typeCode;

    @Schema(
            description = "Device model name",
            example = "Smart Thermostat v2"
    )
    private String model;

    @Schema(
            description = "Physical location of the device",
            example = "Living Room"
    )
    private String location;

    @Schema(
            description = "Current status of the device",
            example = "ACTIVE"
    )
    private String status;

    @Schema(
            description = "Additional metadata as JSON object",
            example = "{\"firmware\": \"1.2.3\", \"features\": [\"temperature\", \"humidity\"]}"
    )
    private Object metadata;

    @Schema(
            description = "Home ID where the device is located",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID homeId;

    @Schema(
            description = "Owner account ID",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID ownerAccount;

    // Конструкторы
    public DeviceCreateRequest() {}

    // Геттеры и сеттеры
    public String getDeviceKey() { return deviceKey; }
    public void setDeviceKey(String deviceKey) { this.deviceKey = deviceKey; }

    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Object getMetadata() { return metadata; }
    public void setMetadata(Object metadata) { this.metadata = metadata; }

    public UUID getHomeId() { return homeId; }
    public void setHomeId(UUID homeId) { this.homeId = homeId; }

    public UUID getOwnerAccount() { return ownerAccount; }
    public void setOwnerAccount(UUID ownerAccount) { this.ownerAccount = ownerAccount; }
}