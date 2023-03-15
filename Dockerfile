#FROM openjdk:17
FROM eclipse-temurin:17

ARG JAR_FILE
COPY bluecove/bluecove-2.1.0.jar bluecove-2.1.0.jar
COPY bluecove/bluecove-gpl-2.1.0.jar bluecove-gpl-2.1.0.jar
COPY ${JAR_FILE} app.jar

# RUN ["apt-get", "install", "unzip"]

# REQUIRED FOR BLUETOOTH TO WORK
RUN apt-get update
RUN apt install -y bluez bluetooth libbluetooth-dev bluecove ant

#ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["java", "-cp", "bluecove-2.1.0.jar:bluecove-gpl-2.1.0.jar", "-jar","/app.jar"]
