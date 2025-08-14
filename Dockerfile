# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"
# If your jar ends with -SNAPSHOT use the wildcard; otherwise replace with exact name
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
CMD ["java","-jar","/app/app.jar"]
