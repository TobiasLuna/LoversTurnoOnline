FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM  eclipse-temurin:23-alpine
COPY --from=build /target/turnos-0.0.1-SNAPSHOT.jar turnos.jar
EXPOSE 8080
ENTRYPOINT [ "java", "jar", "turnos.jar" ]