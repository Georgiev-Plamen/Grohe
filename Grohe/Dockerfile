# Use an official OpenJDK runtime as the base image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/Grohe-0.0.2.jar Grohe-0.0.2.jar

EXPOSE 8080

# Command to run your application
ENTRYPOINT ["java", "-jar", "Grohe-0.0.2.jar"]