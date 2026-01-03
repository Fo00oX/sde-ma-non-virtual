# --- Build ---
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml

COPY sde.ma.non.virtual.aggregator/pom.xml ./sde.ma.non.virtual.aggregator/pom.xml
COPY sde.ma.non.virtual.downstream/pom.xml ./sde.ma.non.virtual.downstream/pom.xml

RUN mkdir -p sde.ma.non.virtual.aggregator sde.ma.non.virtual.downstream

RUN mvn -q -DskipTests=true dependency:go-offline

COPY sde.ma.non.virtual.aggregator/src ./sde.ma.non.virtual.aggregator/src

RUN mvn -q -DskipTests=true -pl sde.ma.non.virtual.aggregator -am clean package

# --- Run ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /workspace/sde.ma.non.virtual.aggregator/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
