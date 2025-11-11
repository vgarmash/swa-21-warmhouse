# Мульти-стадийная сборка
FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /app

# Копируем файлы проекта
COPY pom.xml .
COPY src ./src

# Устанавливаем Maven и собираем приложение
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:21-jre-jammy

RUN useradd -m -s /bin/bash appuser
USER appuser

WORKDIR /app

# Копируем JAR из стадии сборки
COPY --from=builder /app/target/telemetry-service-1.0.0.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]