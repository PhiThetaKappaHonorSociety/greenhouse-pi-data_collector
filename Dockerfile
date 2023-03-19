#FROM --platform=linux/amd64 openjdk:12.0.2
#FROM --platform=$BUILDPLATFORM openjdk:12.0.2
FROM openjdk:12.0.2

VOLUME /tmp
VOLUME /target

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# RUN ["apt-get", "install", "unzip"]

# REQUIRED FOR BLUETOOTH TO WORK
#RUN ["apt-get", "update"]
#RUN ["apt-get", "install", "libbluetooth-dev"]

ENTRYPOINT ["java","-jar","/app.jar"]
