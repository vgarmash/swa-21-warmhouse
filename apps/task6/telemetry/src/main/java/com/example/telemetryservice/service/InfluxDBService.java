package com.example.telemetryservice.service;

import com.example.telemetryservice.model.TelemetryData;
import com.example.telemetryservice.model.TelemetrySearchRequest;
import com.example.telemetryservice.model.DeviceType;
import com.example.telemetryservice.model.DataType;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class InfluxDBService {

    private static final Logger logger = Logger.getLogger(InfluxDBService.class.getName());

    private final InfluxDBClient influxDBClient;
    private final String bucket;
    private final String org;
    private final String measurementName;

    public InfluxDBService(
            InfluxDBClient influxDBClient,
            @Value("${influxdb.bucket}") String bucket,
            @Value("${influxdb.org}") String org,
            @Value("${influxdb.measurement-name}") String measurementName) {
        this.influxDBClient = influxDBClient;
        this.bucket = bucket;
        this.org = org;
        this.measurementName = measurementName;
    }

    public void saveTelemetryData(TelemetryData telemetryData) {
        try {
            Point point = Point.measurement(measurementName)
                    .addTag("source_id", telemetryData.sourceId().toString())
                    .addTag("source_type", telemetryData.sourceType().name())
                    .addTag("data_type", telemetryData.dataType().name())
                    .addField("value", telemetryData.value())
                    .time(telemetryData.timestamp(), WritePrecision.MS);

            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(point);

            logger.info("Telemetry data saved: " + telemetryData);
        } catch (Exception e) {
            logger.severe("Error saving telemetry data to InfluxDB: " + e.getMessage());
            throw new RuntimeException("Failed to save telemetry data", e);
        }
    }

    public List<TelemetryData> searchTelemetryData(TelemetrySearchRequest searchRequest) {
        StringBuilder fluxQuery = new StringBuilder();
        fluxQuery.append(String.format("from(bucket: \"%s\") ", bucket));
        fluxQuery.append("|> range(start: ");
        fluxQuery.append(searchRequest.startTime() != null ?
                searchRequest.startTime().toString() : "-30d");
        fluxQuery.append(", stop: ");
        fluxQuery.append(searchRequest.endTime() != null ?
                searchRequest.endTime().toString() : "now()");
        fluxQuery.append(") ");
        fluxQuery.append(String.format("|> filter(fn: (r) => r._measurement == \"%s\") ", measurementName));

        if (searchRequest.sourceIds() != null && !searchRequest.sourceIds().isEmpty()) {
            String sourceIds = String.join("\", \"",
                    searchRequest.sourceIds().stream().map(String::valueOf).toList());
            fluxQuery.append(String.format("|> filter(fn: (r) => contains(value: r.source_id, set: [\"%s\"])) ", sourceIds));
        }

        if (searchRequest.sourceType() != null) {
            fluxQuery.append(String.format("|> filter(fn: (r) => r.source_type == \"%s\") ",
                    searchRequest.sourceType().name()));
        }

        if (searchRequest.dataType() != null) {
            fluxQuery.append(String.format("|> filter(fn: (r) => r.data_type == \"%s\") ",
                    searchRequest.dataType().name()));
        }

        fluxQuery.append("|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\") ");

        if (searchRequest.limit() != null) {
            fluxQuery.append(String.format("|> limit(n: %d) ", searchRequest.limit()));
        }

        try {
            List<FluxTable> tables = influxDBClient.getQueryApi().query(fluxQuery.toString(), org);
            List<TelemetryData> telemetryDataList = new ArrayList<>();

            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    TelemetryData telemetryData = mapRecordToTelemetryData(record);
                    if (telemetryData != null) {
                        telemetryDataList.add(telemetryData);
                    }
                }
            }

            return telemetryDataList;
        } catch (Exception e) {
            logger.severe("Error querying telemetry data from InfluxDB: " + e.getMessage());
            throw new RuntimeException("Failed to query telemetry data", e);
        }
    }

    private TelemetryData mapRecordToTelemetryData(FluxRecord record) {
        try {
            Long sourceId = Long.valueOf(record.getValueByKey("source_id").toString());
            DeviceType sourceType = DeviceType.valueOf(record.getValueByKey("source_type").toString());
            DataType dataType = DataType.valueOf(record.getValueByKey("data_type").toString());
            String value = record.getValueByKey("value").toString();
            Instant timestamp = Instant.parse(record.getValueByKey("_time").toString());

            return new TelemetryData(sourceId, sourceType, value, dataType, timestamp);
        } catch (Exception e) {
            logger.warning("Error mapping FluxRecord to TelemetryData: " + e.getMessage());
            return null;
        }
    }
}