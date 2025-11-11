package com.example.devicemanagement.service;

import com.example.devicemanagement.dto.DeviceCreateRequest;
import com.example.devicemanagement.model.Device;
import com.example.devicemanagement.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MqttService mqttService;

    public Device registerDevice(DeviceCreateRequest request) {
        // Проверка на существование устройства с таким deviceKey
        if (deviceRepository.findByDeviceKey(request.getDeviceKey()).isPresent()) {
            throw new IllegalArgumentException("Device with key " + request.getDeviceKey() + " already exists");
        }

        Device device = new Device();
        device.setDeviceKey(request.getDeviceKey());
        device.setTypeCode(request.getTypeCode());
        device.setModel(request.getModel());
        device.setLocation(request.getLocation());
        device.setStatus(request.getStatus());
        device.setMetadata(request.getMetadata() != null ? request.getMetadata().toString() : null);
        device.setHomeId(request.getHomeId());
        device.setOwnerAccount(request.getOwnerAccount());

        Device savedDevice = deviceRepository.save(device);

        // Отправка события в MQTT
        mqttService.publishDeviceEvent("registered", savedDevice);

        return savedDevice;
    }

    @Transactional(readOnly = true)
    public List<Device> listDevices(UUID homeId, String typeCode, String location) {
        if (homeId != null) {
            return deviceRepository.findByHomeId(homeId);
        } else if (typeCode != null) {
            return deviceRepository.findByTypeCode(typeCode);
        } else if (location != null) {
            return deviceRepository.findByLocation(location);
        } else {
            return deviceRepository.findAll();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Device> getDeviceById(UUID id) {
        return deviceRepository.findById(id);
    }
}