FROM maven:3.9-eclipse-temurin-21
WORKDIR /app
RUN apt-get update && \
    apt-get install -y docker.io && \
    rm -rf /var/lib/apt/lists/*
COPY . .
RUN mvn clean package -DskipTests
RUN cp target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080