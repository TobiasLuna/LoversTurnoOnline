FROM openjdk:21

COPY ./target/turnos-0.0.1-SNAPSHOT.jar turnos.jar

EXPOSE 8080

ENTRYPOINT [ "java" , "-jar" , "/app.jar" ]