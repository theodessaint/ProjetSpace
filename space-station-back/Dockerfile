# Image de base à partir de laquelle notre nouvelle image sera créée
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/space-station-back-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080


