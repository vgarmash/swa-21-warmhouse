package com.example.devicemanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "devices")
@Schema(description = "Device entity representing an IoT device in the system")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(
            description = "Unique identifier of the device",
            example = "123e4567-e89b-12d3-a456-426614174000",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;

    @Column(name = "device_key", nullable = false, unique = true)
    @Schema(
            description = "Unique device key identifier",
            example = "device-12345"
    )
    private String deviceKey;

    @Column(name = "type_code", nullable = false)
    @Schema(
            description = "Type code of the device",
            example = "THERMOSTAT"
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
            example = "ACTIVE",
            allowableValues = {"ACTIVE", "INACTIVE", "OFFLINE", "MAINTENANCE"}
    )
    private String status;

    @Column(columnDefinition = "TEXT")
    @Schema(
            description = "Additional metadata as JSON string",
            example = "{\"firmware\": \"1.2.3\", \"features\": [\"temperature\", \"humidity\"]}"
    )
    private String metadata;

    @Column(name = "home_id")
    @Schema(
            description = "Home ID where the device is located",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID homeId;

    @Column(name = "owner_account")
    @Schema(
            description = "Owner account ID",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID ownerAccount;

    // Конструкторы, геттеры и сеттеры остаются без изменений
    public Device() {}

    public Device(String deviceKey, String typeCode, String model, String location,
                  String status, String metadata, UUID homeId, UUID ownerAccount) {
        this.deviceKey = deviceKey;
        this.typeCode = typeCode;
        this.model = model;
        this.location = location;
        this.status = status;
        this.metadata = metadata;
        this.homeId = homeId;
        this.ownerAccount = ownerAccount;
    }

    // Геттеры и сеттеры
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public UUID getHomeId() { return homeId; }
    public void setHomeId(UUID homeId) { this.homeId = homeId; }

    public UUID getOwnerAccount() { return ownerAccount; }
    public void setOwnerAccount(UUID ownerAccount) { this.ownerAccount = ownerAccount; }
}