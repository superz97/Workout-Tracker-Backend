# Build stage
FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle build -x test --no-daemon || return 0
COPY src ./src
RUN gradle build -x test --no-daemon

# Runtime stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# Create non-root user
RUN groupadd -g 1000 appgroup && \
    useradd -r -u 1000 -g appgroup appuser && \
    mkdir -p /app/logs && \
    chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]