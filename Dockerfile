FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/gestao-funcionarios-1.0.0.jar app.jar
ENV SPRING_PROFILES_ACTIVE=render
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
