# Stage 1: Build the JAR with Maven
FROM maven:3.9-eclipse-temurin-21 AS build

ARG APP_VERSION
WORKDIR /workspace
COPY pom.xml ./
RUN test -n "${APP_VERSION}" \
    && mvn --batch-mode -Drevision="${APP_VERSION}" dependency:go-offline
COPY src src
RUN test "$(mvn -Drevision="${APP_VERSION}" help:evaluate -Dexpression=project.version -q -DforceStdout)" = "${APP_VERSION}" \
    && mvn --batch-mode -Drevision="${APP_VERSION}" clean package -DskipTests \
    && cp "target/simple-crm-${APP_VERSION}.jar" target/app.jar

# Stage 2: Run the JAR in a slim runtime image   
FROM eclipse-temurin:21-jre-alpine AS runtime

ARG APP_VERSION
ARG VCS_REF=unknown
LABEL org.opencontainers.image.title="Simple CRM API" \
      org.opencontainers.image.version="${APP_VERSION}" \
      org.opencontainers.image.revision="${VCS_REF}"

RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app
COPY --from=build --chown=spring:spring /workspace/target/app.jar /app/app.jar

USER spring:spring
ENV PORT=8080 \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
