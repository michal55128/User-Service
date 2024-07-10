# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the target directory to the container
COPY target/user-login-0.0.1-SNAPSHOT.jar /app/user-login.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/user-login.jar"]