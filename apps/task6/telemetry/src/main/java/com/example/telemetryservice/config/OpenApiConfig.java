package com.example.telemetryservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:telemetry-service}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Telemetry Service API")
                        .version("1.0.0")
                        .description("""
                            Микросервис для работы с телеметрическими данными через MQTT и REST API.
                            
                            ## Health Checks
                            - **Actuator Health**: `GET /actuator/health`
                            - **Service Info**: `GET /actuator/info`
                            - **Metrics**: `GET /actuator/metrics`
                            
                            ## API Documentation
                            - **Swagger UI**: `GET /swagger-ui.html`
                            - **OpenAPI Spec**: `GET /api-docs`
                            """)
                        .contact(new Contact()
                                .name("Telemetry Service Team")
                                .email("vgarmash@yandex.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Локальный сервер"),
                        new Server()
                                .url("http://telemetry-service:8080")
                                .description("Docker сервер")
                ));
    }
}