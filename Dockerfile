# Use Eclipse Temurin - the modern standard for OpenJDK
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 5477
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=5477"]