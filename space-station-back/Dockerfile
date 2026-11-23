# Image de base à partir de laquelle notre nouvelle image sera créée
FROM maven:eclipse-temurin:21-jre

WORKDIR /app

COPY target/space-station-back-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]


