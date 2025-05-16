# Build stage
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Copy build files first for better caching
COPY gradlew .
COPY gradle/wrapper gradle/wrapper
COPY build.gradle .
COPY settings.gradle .

# Download dependencies (separate layer for caching)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build application with reproducible builds
RUN ./gradlew clean bootJar --no-daemon --stacktrace

# Verify JAR exists before copying
RUN ls -la /app/build/libs/

# Run stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Explicitly copy the Spring Boot JAR (better than wildcard)
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

# Create non-root user and set permissions
RUN useradd -m myuser && \
    chown -R myuser:myuser /app && \
    chmod 750 /app
USER myuser

EXPOSE 8080

# External database configuration (for development)
ENV MYSQL_HOST=host.docker.internal \
    MYSQL_PORT=3306 \
    MYSQL_DATABASE=order-deplan

# Use exec form without shell for better signal handling
ENTRYPOINT ["java", "-jar", "app.jar"]