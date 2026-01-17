# --- Build ---
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -q -DskipTests=true dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests=true clean package

# --- Run ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8081
ENTRYPOINT ["java","-Dserver.port=${SERVER_PORT:-8081}","-Dspring.application.name=${SPRING_APPLICATION_NAME:-app}","-jar","/app/app.jar"]
