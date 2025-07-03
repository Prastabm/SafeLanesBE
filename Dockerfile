FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/services-0.0.1-SNAPSHOT.jar app.jar

# Port 8085 to match application.properties
EXPOSE 8085

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8085/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]