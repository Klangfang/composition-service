Welcome to the composition service - the application core system of the klangfang app.

This project is based on the Spring Boot Framework and Java.

To build this project and run the application without the docker container (i.e. in the host OS):
````
./mvnw package && java -jar target/composition-service-1.0.jar

````
Building a new tagged docker image:
````
./mvnw install dockerfile:build

````

Running the tagged docker image:
````
docker run -p 8080:8080 -t casasky/composition-service

````

