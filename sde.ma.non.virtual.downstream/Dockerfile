# --- Build ---
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace

# Copy root pom
COPY pom.xml ./pom.xml

# Copy BOTH module poms so the parent reactor resolves modules
COPY sde.ma.non.virtual.downstream/pom.xml ./sde.ma.non.virtual.downstream/pom.xml
COPY sde.ma.non.virtual.aggregator/pom.xml ./sde.ma.non.virtual.aggregator/pom.xml

# (Optional but safe) Ensure module directories exist even if only pom was copied
RUN mkdir -p sde.ma.non.virtual.downstream sde.ma.non.virtual.aggregator

# Go offline now that reactor is complete
RUN mvn -q -DskipTests=true dependency:go-offline

# Copy sources only for downstream
COPY sde.ma.non.virtual.downstream/src ./sde.ma.non.virtual.downstream/src

# Build downstream module
RUN mvn -q -DskipTests=true -pl sde.ma.non.virtual.downstream -am clean package

# --- Run ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Prefer a single jar name; see note below
COPY --from=build /workspace/sde.ma.non.virtual.downstream/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
