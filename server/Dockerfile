FROM maven:3.9-eclipse-temurin-21-alpine
WORKDIR /app
RUN apk add --no-cache docker-cli
COPY . .
RUN mvn clean package -DskipTests
RUN cp target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080