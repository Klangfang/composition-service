Welcome to the composition service - the application core system of the klangfang app.
It provides many functionalities, such as saving a composition, collaborating with a composition and listing all available compositions.
The klangfang android app is communicating directly with the composition service via rest api.
The service is actually hosted in heroku.


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

