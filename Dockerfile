FROM ubuntu:latest
FROM openjdk:19-jdk-alpine

COPY keystore.p12 keystore.p12
COPY target/redis_service-0.0.1-SNAPSHOT.jar redis_service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/redis_service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8079
