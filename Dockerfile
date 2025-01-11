FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/turnos-0.0.1-SNAPSHOT.jar turnos.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","turnos.jar"]