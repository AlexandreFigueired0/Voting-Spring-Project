FROM docker.io/maven:3-eclipse-temurin-17
ADD pom.xml .
RUN mvn dependency:go-offline
COPY . .
ENTRYPOINT mvn clean test
