# Build stage
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Set Gradle options to prevent memory issues
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx1024m"

# Copy Gradle wrapper files first (better caching)
COPY gradlew .
COPY gradle/wrapper gradle/wrapper
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable and verify
RUN chmod +x gradlew && ./gradlew --version

# Copy source code
COPY src src

# Build application with debug options
RUN ./gradlew clean build --stacktrace --info

# Run stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy built artifact
COPY --from=builder /app/build/libs/*.jar app.jar

# Security: Run as non-root user
RUN addgroup --system spring && \
    adduser --system --ingroup spring spring
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]