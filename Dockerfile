# FROM --platform=linux/amd64 openjdk:11-jdk
FROM --platform=linux/amd64 gcr.io/distroless/java:11

VOLUME /tmp
VOLUME /target

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# RUN ["apt-get", "install", "unzip"]

ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
