# Image de base à partir de laquelle notre nouvelle image sera créée
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]


