# Use Eclipse Temurin JDK for build stage
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy pom.xml first (for better layer caching)
COPY pom.xml .

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage - use JRE for smaller image
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Create non-root user for security (Alpine Linux syntax)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN chown -R appuser:appgroup /app
USER appuser

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]