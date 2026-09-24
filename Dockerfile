# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy gradle wrapper and configuration files for dependency caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Grant execute permission for gradlew
RUN chmod +x ./gradlew

# Pre-fetch dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code and build the application JAR
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# Stage 2: Create lightweight production runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the compiled executable JAR from the builder stage
COPY --from=builder /app/build/libs/main-*.jar app.jar

# Expose server port
EXPOSE 8080

# Environment variables (default to memory tuning)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Launch application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
