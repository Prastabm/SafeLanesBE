# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built jar file
COPY target/services-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8085

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]