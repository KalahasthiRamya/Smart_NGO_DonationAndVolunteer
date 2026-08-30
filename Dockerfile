# ==================================================
# Smart NGO Management Platform - Multi-Stage Dockerfile
# Stage 1: Build Java artifact using Maven & Temurin JDK 8
# Stage 2: Production Eclipse Temurin 8 JRE Runtime Image
# ==================================================

# Stage 1: Build
FROM maven:3.8.8-eclipse-temurin-8 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:8-jre
WORKDIR /app
COPY --from=builder /app/target/smart-ngo-platform-1.0.0.jar app.jar

# Environment defaults
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms128m -Xmx384m -XX:+UseSerialGC"

EXPOSE ${PORT}

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
