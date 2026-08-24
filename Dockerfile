# ==================================================
# Smart NGO Management Platform - Multi-Stage Dockerfile
# Stage 1: Build Java artifact using Maven
# Stage 2: Production OpenJDK 8 Runtime Image
# ==================================================

# Stage 1: Build
FROM maven:3.8.6-openjdk-8-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Runtime
FROM openjdk:8-jre-slim
WORKDIR /app
COPY --from=builder /app/target/smart-ngo-platform-1.0.0.jar app.jar

# Environment defaults
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE ${PORT}

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
