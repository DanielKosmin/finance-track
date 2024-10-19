# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle ./
COPY gradle gradle

# Copy the source code
COPY src ./src

# Copy the Gradle Wrapper files
COPY gradlew gradlew
COPY gradle/wrapper/ gradle/wrapper/

# Make the gradlew script executable
RUN chmod +x gradlew

# Build the application using the Gradle Wrapper
RUN ./gradlew build

# Copy the built JAR file to the container
COPY build/libs/finance_tracker-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
