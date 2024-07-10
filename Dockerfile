
FROM maven:3.8.6-openjdk-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/user-login-0.0.1-SNAPSHOT.jar /app/user-login.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/user-login.jar"]


