FROM ubuntu:latest
LABEL authors="micha"

FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .docker build -t user-service ./

COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar","top", "-b"]
