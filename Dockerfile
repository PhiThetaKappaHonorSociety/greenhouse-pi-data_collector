# FROM --platform=linux/amd64 gcr.io/distroless/java:11
# FROM --platform=linux/arm/v7 openjdk:11-jdk
FROM --platform=linux/arm64/v8 openjdk:20-jdk-slim-buster

VOLUME /tmp
VOLUME /target

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# RUN ["apt-get", "install", "unzip"]

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
