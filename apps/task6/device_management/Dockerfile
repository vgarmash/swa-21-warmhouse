# Dockerfile
FROM maven:3.8-openjdk-17 AS build

WORKDIR /app

# Копируем pom.xml и загружаем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код и собираем приложение
COPY src ./src
RUN mvn clean package -DskipTests

# Финальный образ
FROM openjdk:17-jdk-slim

WORKDIR /app

# Устанавливаем переменные для JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE="docker"

# Создаем пользователя для безопасности
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# Копируем собранный JAR из стадии сборки
COPY --from=build /app/target/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]