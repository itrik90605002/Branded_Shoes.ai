# --- Stage 1: Build the Maven Project ---
FROM maven:3.9.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy the pom.xml and dependency definitions first to cache layers
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the package
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Stage 2: Run the Spring Boot App ---
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose server port
EXPOSE 8080

# Run the jar with production profile
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
