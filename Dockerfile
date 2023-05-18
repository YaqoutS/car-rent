FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY target/CarRent-0.0.1-SNAPSHOT.jar /app/carservice.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "carservice.jar"]