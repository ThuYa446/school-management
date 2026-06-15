# ---- 1) Build stage ----
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# ---- 2) Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from stage 1
COPY --from=build /app/target/*.jar app.jar

# Spring Boot default port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
