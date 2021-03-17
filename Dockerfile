FROM openjdk:8-jdk-alpine

COPY ./target/urlshortener-0.0.1-SNAPSHOT.jar /urlshortener.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/urlshortener.jar","--server.port=8080"]
