# === Stage 1: Build ===
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# === Stage 2: Run ===
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create and use non-root user
RUN addgroup -S app && adduser -S app -G app

COPY --from=builder /app/target/*.jar app.jar
RUN chown app:app app.jar

USER app

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
