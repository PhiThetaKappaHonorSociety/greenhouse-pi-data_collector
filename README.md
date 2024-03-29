# greenhouse-pi-data_collector
A round robin service to collect data from sensors in the greenhouse

# To Run
1. Run SensorDataCollector eclipse launch file

# To Build

We build docker container images to run using docker-compose

To build the container
1. Build the jar file 
    Either the "greenhouse - sensor_data_collector - clean install" Eclipse Launch File
    Or run `mvn install` inside the folder
2. Open terminal inside the workspace root directory (it contains the Dockerfile)
3. Build docker image (You will need docker installed on your computer)

```
docker build --build-arg JAR_FILE=target/sensor_data_collector-VERSION-jar-with-dependencies.jar -t tcrokicki/ptk-greenhouse-app-sensor_data_collector:vVERSION .

```

```
docker build --build-arg JAR_FILE=target/sensor_data_collector-1.0.3-jar-with-dependencies.jar -t tcrokicki/ptk-greenhouse-app-sensor_data_collector:v1.0.3.1 .
```

```
docker buildx build --platform=linux/amd64,linux/arm64 --build-arg JAR_FILE=target/sensor_data_collector-1.0.3-jar-with-dependencies.jar  -t tcrokicki/ptk-greenhouse-app-sensor_data_collector:v1.0.3.2 .

```

```
docker buildx build --build-arg JAR_FILE=target/sensor_data_collector-1.0.2-jar-with-dependencies.jar -t tcrokicki/ptk-greenhouse-app-sensor_data_collector:v1.0.2.12 ./
```

4. Push to Docker Hub when ready for release

```
docker push tcrokicki/ptk-greenhouse-app-sensor_data_collector:vVERSION
```



# Useful commands

### Sign in to docker hub
`docker login --username USERNAME`

### To run container
`docker run --env xxx='zzzz' tcrokicki/ptk-greenhouse-app-sensor_data_collector:VERSION`