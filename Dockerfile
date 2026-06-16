# Estágio 1: Build da aplicação usando Gradle e Java 26
FROM gradle:jdk26 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

# Estágio 2: Execução da aplicação (Usando Temurin Java 26 Alpino/Slim)
FROM eclipse-temurin:26-jre
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/pulgatti-weather.jar
ENTRYPOINT ["java", "-jar", "/app/pulgatti-weather.jar"]