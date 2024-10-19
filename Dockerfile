# Stage 1: Build the application
FROM gradle:8.10.2-jdk17 AS builder

WORKDIR /app
COPY . .
RUN ./gradlew clean build

# Stage 2: Create a minimal runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=builder /app/build/libs/finance_tracker-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
