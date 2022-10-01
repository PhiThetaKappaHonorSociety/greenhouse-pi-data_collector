# FROM --platform=linux/amd64 gcr.io/distroless/java:11
# FROM --platform=linux/arm/v7 openjdk:11-jdk
# FROM --platform=linux/arm64/v8 openjdk:20-jdk-slim-buster


# FROM --platform=linux/amd64 openjdk:11-jdk
# FROM --platform=linux/amd64 gcr.io/distroless/java:11
# FROM arm32v7/adoptopenjdk:11-jre-hotspot-focal
FROM eclipse-temurin:19_36-jre-jammy

#FROM debian:buster-slim
#ENV JAVA_HOME=/opt/java/openjdk
#COPY --from=eclipse-temurin:11 $JAVA_HOME $JAVA_HOME
#ENV PATH="${JAVA_HOME}/bin:${PATH}"



VOLUME /tmp
VOLUME /target

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# RUN ["apt-get", "install", "unzip"]

ENTRYPOINT ["java","-jar","/app.jar"]
