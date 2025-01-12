FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM  eclipse-temurin:23-alpine
COPY --from=build /target/*.jar turno.jar
EXPOSE 8080
ENTRYPOINT [ "java", "jar", "turno.jar" ]